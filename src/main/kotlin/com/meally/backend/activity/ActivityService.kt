package com.meally.backend.activity

import com.meally.backend.activity.dto.InsertActivityDto
import com.meally.backend.imageResource.ImageResource
import org.springframework.stereotype.Service

@Service
class ActivityService(
    private val activityRepository: ActivityRepository,
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
}