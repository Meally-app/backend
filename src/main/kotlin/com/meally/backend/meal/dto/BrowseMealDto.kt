package com.meally.backend.meal.dto

import com.meally.backend.users.User

data class BrowseMealDto(
    val id: String,
    val name: String,
    val calories: Double,
    val user: User,
    val isLiked: Boolean,
)
