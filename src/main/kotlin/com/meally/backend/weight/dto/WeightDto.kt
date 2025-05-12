package com.meally.backend.weight.dto

import com.meally.backend.weight.UserWeight
import java.time.LocalDate
import java.time.LocalDateTime

data class WeightDto(
    val weight: Double,
    val date: LocalDate,
    val createdAt: LocalDateTime? = null,
)

fun UserWeight.toDto() = WeightDto(
    weight = weight,
    date = date,
    createdAt = createdAt,
)
