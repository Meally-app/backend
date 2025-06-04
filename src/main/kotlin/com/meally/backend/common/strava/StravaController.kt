package com.meally.backend.common.strava

import com.meally.backend.activity.ActivityEntry
import com.meally.backend.activity.ActivityService
import jakarta.servlet.http.HttpSession
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate


@RestController
class StravaController(
    private val stravaService: StravaService,
    private val activityService: ActivityService,
) {

    @PostMapping("/strava/token")
    fun exchangeCodeAndStoreToken(
        @RequestParam code: String,
        session: HttpSession
    ): ResponseEntity<*> {
        stravaService.exchangeCodeForToken(code)
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("/strava/sync")
    fun syncStravaActivities(@RequestParam("date") date: LocalDate): ResponseEntity<List<ActivityEntry>> {
        stravaService.syncStrava(date)
        return ResponseEntity.ok(activityService.getUserActivityEntries())
    }
}