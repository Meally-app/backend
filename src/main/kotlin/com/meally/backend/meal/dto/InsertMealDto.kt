package com.meally.backend.meal.dto

import com.meally.backend.meal.MealVisibility
import java.util.UUID

data class InsertMealDto(
    val mealId: UUID? = null,
    val name: String,
    val visibility: MealVisibility,
    val foodInMeal: List<InsertFoodInMealDto>,
)

data class InsertFoodInMealDto(
    val foodId: UUID,
    val amount: Double,
)