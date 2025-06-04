package com.meally.backend.activity

import com.meally.backend.activity.dto.InsertActivityDto
import com.meally.backend.auth.AuthService
import com.meally.backend.common.strava.StravaService
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.imageResource.ImageResource
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ActivityService(
    private val activityRepository: ActivityRepository,
    private val activityEntryRepository: ActivityEntryRepository,
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

    fun getUserActivityEntries(): List<ActivityEntry> {
        val userId = authService.getLoggedInUser()?.id ?: throw ResourceNotFoundException
        return activityEntryRepository.findAllByUserId(userId)
    }

}