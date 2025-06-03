package com.meally.backend.common.strava

import jakarta.servlet.http.HttpSession
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class StravaController(
    private val stravaService: StravaService,
) {

    @PostMapping("/strava/token")
    fun exchangeCodeAndStoreToken(
        @RequestParam code: String,
        session: HttpSession
    ): ResponseEntity<*> {
        stravaService.exchangeCodeForToken(code)
        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("/strava/activities")
    fun stravaTest(): String? {
        return stravaService.getAllActivities()
    }
}