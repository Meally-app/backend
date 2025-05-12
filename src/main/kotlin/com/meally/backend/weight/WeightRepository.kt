package com.meally.backend.weight

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface WeightRepository: JpaRepository<UserWeight, UUID> {
    fun findAllByDateLessThanEqualAndDateGreaterThanEqualAndUserId(lower: LocalDate, upper: LocalDate, userId: UUID?): List<UserWeight>

    fun findByDate(date: LocalDate): UserWeight?
}