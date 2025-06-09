package com.meally.backend.meal.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.util.UUID

data class InsertMealEntryDto(
    val mealId: UUID,
    val mealType: String,
    val amount: Double,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val date: LocalDate,
)
