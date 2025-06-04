package com.meally.backend.diary.dto

import java.time.LocalDate

data class DiarySummaryDayDto(
    val date: LocalDate,
    val calories: Double,
)
