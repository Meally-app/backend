package com.meally.backend.meal

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface MealEntryRepository : JpaRepository<MealEntry, UUID> {

    fun findAllByUserIdAndDate(userId: UUID, date: LocalDate): List<MealEntry>

    fun findAllByDateLessThanEqualAndDateGreaterThanEqualAndUserId(to: LocalDate, from: LocalDate, userId: UUID?): List<MealEntry>

}