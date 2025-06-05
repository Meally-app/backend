package com.meally.backend.activity

import com.meally.backend.activity.dto.ActivityEntriesForDateDto
import com.meally.backend.activity.dto.InsertActivityDto
import com.meally.backend.auth.AuthService
import com.meally.backend.common.strava.StravaService
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.imageResource.ImageResource
import com.meally.backend.thirdPartyToken.ThirdPartyTokenProvider
import com.meally.backend.thirdPartyToken.ThirdPartyTokenRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ActivityService(
    private val activityRepository: ActivityRepository,
    private val activityEntryRepository: ActivityEntryRepository,
    private val thirdPartyTokenRepository: ThirdPartyTokenRepository,
    private val authService: AuthService,
) {

    fun getAllActivities(): List<Activity> {
        return activityRepository.findAll()
    }

    fun insertActivity(dto: InsertActivityDto): Activity {
        return activityRepository.save(
            Activity(
                name = dto.name,
                image = ImageResource(name = dto.resourceName)
            )
        )
    }

    fun getActivityEntriesForDate(date: LocalDate): ActivityEntriesForDateDto {
        val userId = authService.getLoggedInUser()?.id ?: throw ResourceNotFoundException
        val isAuthorized = thirdPartyTokenRepository.findByUserIdAndProvider(userId, ThirdPartyTokenProvider.STRAVA) != null

        return ActivityEntriesForDateDto(
            exercise = activityEntryRepository.findAllByUserIdAndDate(userId, date),
            isAuthorized = isAuthorized,
        )
    }
    fun getUserActivityEntries(): List<ActivityEntry> {
        val userId = authService.getLoggedInUser()?.id ?: throw ResourceNotFoundException
        return activityEntryRepository.findAllByUserId(userId)
    }

}