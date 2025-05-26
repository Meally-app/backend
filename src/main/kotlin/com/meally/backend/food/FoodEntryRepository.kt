package com.meally.backend.food

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface FoodEntryRepository : JpaRepository<FoodEntry, UUID> {
    fun findAllByUserIdAndDate(userId: UUID, date: LocalDate): List<FoodEntry>

    fun findAllByDateLessThanEqualAndDateGreaterThanEqualAndUserId(to: LocalDate, from: LocalDate, userId: UUID?): List<FoodEntry>

}