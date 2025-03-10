package com.meally.backend.users

import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository,
) {
    fun findOrCreateUser(
        externalId: String,
        name: String?,
        email: String,
        pictureUrl: String?
    ): User {
        val user = userRepository.findByExternalId(externalId)
        if (user != null) return user

        return userRepository.save(
            User(
                externalId = externalId,
                username = name ?: email, // default the username to be the same as email
                email = email,
                profilePicUrl = pictureUrl,
            )
        )
    }
}