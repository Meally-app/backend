package com.meally.backend.meal.dto

import com.meally.backend.meal.FoodInMeal
import com.meally.backend.meal.Meal
import com.meally.backend.meal.MealVisibility
import com.meally.backend.users.User
import java.util.UUID

data class MealDetailsDto(
    val id: UUID?,
    val name: String,
    val user: User,
    val status: MealVisibility,
    val foodInMeal: List<FoodInMeal>,
    val isLiked: Boolean,
)

fun Meal.toDto(isLiked: Boolean) = MealDetailsDto(
    id = id,
    name = name,
    user = user,
    status = status,
    foodInMeal = foodInMeal,
    isLiked = isLiked,
)
