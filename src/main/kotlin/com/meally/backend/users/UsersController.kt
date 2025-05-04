package com.meally.backend.users

import com.meally.backend.auth.AuthService
import com.meally.backend.common.util.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UsersController(
    private val authService: AuthService,
) {
    @GetMapping("/public/test")
    fun publicTest(): ResponseEntity<ApiResponse<String>> {
        return ResponseEntity.ok(ApiResponse("api is working"))
    }

    @GetMapping("/test")
    fun test(): ResponseEntity<ApiResponse<String>> {
        return ResponseEntity.ok(ApiResponse("api is working and you are logged in with email: ${authService.getLoggedInUser()?.email}"))
    }

    @GetMapping("/users/me")
    fun me(): ResponseEntity<User> {
        return ResponseEntity.ok(authService.getLoggedInUser())
    }
}