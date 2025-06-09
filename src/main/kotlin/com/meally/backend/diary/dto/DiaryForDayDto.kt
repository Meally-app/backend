package com.meally.backend.diary.dto

import com.meally.backend.activity.ActivityEntry
import com.meally.backend.food.FoodEntry
import com.meally.backend.meal.MealEntry

data class DiaryForDayDto(
    val goalCalories: Int,
    val food: List<FoodEntry>,
    val exercise: List<ActivityEntry>,
    val meals: List<MealEntry>,
)
