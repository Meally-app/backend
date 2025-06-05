package com.meally.backend.common.strava

import com.meally.backend.activity.ActivityEntry
import com.meally.backend.activity.ActivityEntryRepository
import com.meally.backend.activity.ActivityRepository
import com.meally.backend.auth.AuthService
import com.meally.backend.common.strava.dto.StravaActivityDetailsDto
import com.meally.backend.common.strava.dto.StravaActivitySummaryDto
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.exception.model.ThirdPartyAuthError
import com.meally.backend.thirdPartyToken.ThirdPartyToken
import com.meally.backend.thirdPartyToken.ThirdPartyTokenProvider
import com.meally.backend.thirdPartyToken.ThirdPartyTokenRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class StravaService (
    private val authService: AuthService,
    private val activityEntryRepository: ActivityEntryRepository,
    private val thirdPartyTokenRepository: ThirdPartyTokenRepository,
    private val activityRepository: ActivityRepository,
){
    private val logger: Logger = LoggerFactory.getLogger(StravaService::class.java)

    @Value("\${strava.client-id}")
    private var clientId: String = ""

    @Value("\${strava.client-secret}")
    private var clientSecret: String = ""


    private val restClient = RestClient.builder()
        .baseUrl("https://www.strava.com")
        .build()

    fun syncStrava(date: LocalDate) {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val userId = user.id ?: throw ResourceNotFoundException
        val isAuthorizedOnStrava = thirdPartyTokenRepository.findByUserIdAndProvider(userId, ThirdPartyTokenProvider.STRAVA) != null

        if (!isAuthorizedOnStrava) return

        val allEntries = activityEntryRepository.findAll()
        val checkForSyncEntries = allEntries.filter {
            it.date.isAfter(date.minusWeeks(4)) &&
                it.date.isBefore(date.plusDays(1))
        }

        val shouldSync = checkForSyncEntries.isEmpty() || checkForSyncEntries.firstOrNull {
            it.lastSyncedAt.isBefore(LocalDateTime.now().minusDays(1))
        } != null

        if (!shouldSync) return
        logger.info("Syncing strava activities, user: ${user.email}, date: $date")

        val dateInstant = Instant.ofEpochSecond(date.plusDays(1).atTime(23, 59).toEpochSecond(ZoneOffset.UTC))
        val freshActivities = getAllActivities(
            from = dateInstant.minusSeconds(60 * 60 * 24 * 7 * 4), // fetch for last 4 weeks
            to = dateInstant,
        )

        val allActivities = activityRepository.findAll()
        val unknownActivity = allActivities.find { it.name.equals("unknown", ignoreCase = true) } ?: run { logger.info("Error fetching unknown activity"); return }

        val freshDetails = freshActivities
            .asSequence()
            .map {
                runCatching { getActivityDetails(it.id.toString()) }
            }
            .mapNotNull { it.getOrNull() }
            .map { stravaActivity ->
                ActivityEntry(
                    id = allEntries.firstOrNull { stravaActivity.id.toString() == it.externalId }?.id,
                    user = user,
                    date = stravaActivity.startDateLocal.atZone(ZoneOffset.UTC).toLocalDate(),
                    name = stravaActivity.name,
                    timeSpent = stravaActivity.elapsedTime,
                    caloriesBurnt = stravaActivity.calories,
                    externalId = stravaActivity.id.toString(),
                    activity = allActivities.find { it.name.equals(stravaActivity.sportType, ignoreCase = true) } ?: unknownActivity,
                    lastSyncedAt = LocalDateTime.now(),
                )
            }
            .toList()

        activityEntryRepository.saveAll(freshDetails)

    }

    fun exchangeCodeForToken(code: String): ThirdPartyToken {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val tokenInfo = getStravaAuthData(code, StravaGrantType.AUTHORIZATION_CODE)
        return thirdPartyTokenRepository.save(
            ThirdPartyToken(
                user = user,
                provider = ThirdPartyTokenProvider.STRAVA,
                accessToken = tokenInfo.accessToken,
                refreshToken = tokenInfo.refreshToken,
                expiresAt = Instant.ofEpochSecond(tokenInfo.expiresAt),
            )
        )
    }

    fun getAllActivities(from: Instant, to: Instant): List<StravaActivitySummaryDto> = makeStravaApiCall { accessToken ->
        val result = runCatching {
            restClient.get()
                .uri { builder ->
                    builder.path("/api/v3/activities")
                        .queryParam("after", from.epochSecond)
                        .queryParam("before", to.epochSecond)
                        .build()
                }
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
                .body(object : ParameterizedTypeReference<List<StravaActivitySummaryDto>>() {})
        }

        if (result.isFailure) {
            val error = result.exceptionOrNull()
            logger.info("Error: $error")
        }

        result.getOrNull() ?: listOf()
    }

    fun getActivityDetails(id: String): StravaActivityDetailsDto? = makeStravaApiCall { accessToken ->
        val result = runCatching {
            restClient.get()
                .uri("/api/v3/activities/" + id)
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
                .body(StravaActivityDetailsDto::class.java)
        }

        if (result.isFailure) {
            val error = result.exceptionOrNull()
            logger.info("Error: $error")
        }

        result.getOrNull()
    }

    private fun refreshToken(): ThirdPartyToken {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val userId = user.id ?: throw ResourceNotFoundException
        val tokenData = thirdPartyTokenRepository.findByUserIdAndProvider(userId, ThirdPartyTokenProvider.STRAVA) ?: throw ResourceNotFoundException
        val newAuthData = getStravaAuthData(tokenData.refreshToken, StravaGrantType.REFRESH_TOKEN)
        logger.info("Successfully obtained new access token for ${user.email}")
        return thirdPartyTokenRepository.save(
            tokenData.copy(
                accessToken = newAuthData.accessToken,
                refreshToken = newAuthData.refreshToken,
                expiresAt = Instant.ofEpochSecond(newAuthData.expiresAt),
            )
        )
    }

    private fun getStravaAuthData(code: String, grantType: StravaGrantType): StravaAuthData {
        val formData: MultiValueMap<String, String> = LinkedMultiValueMap()
        formData.add("client_id", clientId)
        formData.add("client_secret", clientSecret)
        when(grantType) {
            StravaGrantType.AUTHORIZATION_CODE -> formData.add("code", code)
            StravaGrantType.REFRESH_TOKEN -> formData.add("refresh_token", code)
        }
        formData.add("grant_type", grantType.propertyName)
        val result = runCatching {
            restClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(StravaAuthData::class.java)
        }

        if (result.isFailure) {
            val error = result.exceptionOrNull()
            logger.info("Error: $error")
        }

        return result.getOrNull() ?: throw ThirdPartyAuthError
    }

    enum class StravaGrantType(val propertyName: String) {
        AUTHORIZATION_CODE("authorization_code"),
        REFRESH_TOKEN("refresh_token"),
    }

    private fun <T> makeStravaApiCall(block: (String) -> T): T {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val userId = user.id ?: throw ResourceNotFoundException
        val tokenData = thirdPartyTokenRepository.findByUserIdAndProvider(userId, ThirdPartyTokenProvider.STRAVA) ?: throw ResourceNotFoundException

        return if (tokenData.expiresAt.isBefore(Instant.now())) {
            logger.info("Access token expired for user ${user.email}")
            block(refreshToken().accessToken)
        } else {
            block(tokenData.accessToken)
        }
    }
}