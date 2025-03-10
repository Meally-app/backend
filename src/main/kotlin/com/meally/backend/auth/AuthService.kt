package com.meally.backend.auth

import com.meally.backend.users.User
import com.meally.backend.users.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
) {
    fun getLoggedInUser(): User? {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication is UsernamePasswordAuthenticationToken) {
            userRepository.findByExternalId(authentication.name)
        } else {
            null
        }
    }
}