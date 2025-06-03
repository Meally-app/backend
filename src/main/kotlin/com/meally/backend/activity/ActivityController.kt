package com.meally.backend.activity

import com.meally.backend.activity.dto.InsertActivityDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ActivityController(
    private val activityService: ActivityService,
) {

    @GetMapping("/public/activity")
    fun getAllActivities(): ResponseEntity<List<Activity>> {
        return ResponseEntity.ok(activityService.getAllActivities())
    }

    @PostMapping("/public/activity")
    fun insertActivity(@RequestBody dto: InsertActivityDto) : ResponseEntity<Activity> {
        return ResponseEntity.ok(activityService.insertActivity(dto))
    }
}