package com.meally.backend.common.strava

import com.meally.backend.activity.ActivityEntry
import com.meally.backend.activity.ActivityService
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.LocalDate


@RestController
class StravaController(
    private val stravaService: StravaService,
    private val activityService: ActivityService,
) {

    @GetMapping("/public/strava/auth/android")
    fun redirectToAndroidApp(
        @RequestParam code: String,
    ): ResponseEntity<Void> {
        val location = URI.create("meally://strava-auth?code=$code")
        val headers = HttpHeaders()
        headers.location = location
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build()
    }

    @PostMapping("/strava/token")
    fun exchangeCodeAndStoreToken(
        @RequestParam code: String,
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