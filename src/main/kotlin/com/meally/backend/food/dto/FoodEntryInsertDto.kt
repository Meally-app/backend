package com.meally.backend.food.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class FoodEntryInsertDto(
    val foodId: String?,
    val mealType: String,
    val amount: Double,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val date: LocalDate,
)
