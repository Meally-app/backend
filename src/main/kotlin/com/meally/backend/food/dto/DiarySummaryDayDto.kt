package com.meally.backend.food.dto

import java.time.LocalDate

data class DiarySummaryDayDto(
    val date: LocalDate,
    val calories: Double,
)
