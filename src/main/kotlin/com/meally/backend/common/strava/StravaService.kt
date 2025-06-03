package com.meally.backend.common.strava

import com.meally.backend.auth.AuthService
import com.meally.backend.common.openFoodFacts.OpenFoodFactsService
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.exception.model.ThirdPartyAuthError
import com.meally.backend.thirdPartyToken.ThirdPartyToken
import com.meally.backend.thirdPartyToken.ThirdPartyTokenProvider
import com.meally.backend.thirdPartyToken.ThirdPartyTokenRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import java.time.Instant

@Service
class StravaService (
    private val authService: AuthService,
    private val thirdPartyTokenRepository: ThirdPartyTokenRepository,
){
    private val logger: Logger = LoggerFactory.getLogger(OpenFoodFactsService::class.java)

    @Value("\${strava.client-id}")
    private var clientId: String = ""

    @Value("\${strava.client-secret}")
    private var clientSecret: String = ""


    private val restClient = RestClient.builder()
        .baseUrl("https://www.strava.com")
        .build()

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

    fun getAllActivities(): String? = makeStravaApiCall { accessToken ->
        val result = runCatching {
            restClient.get()
                .uri("/api/v3/activities")
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
                .body(String::class.java)
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