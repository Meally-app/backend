package com.meally.backend.food

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.*

interface FoodEntryRepository : JpaRepository<FoodEntry, UUID> {
    fun findAllByUserIdAndDate(userId: UUID, date: LocalDate): List<FoodEntry>

    fun findAllByDateLessThanEqualAndDateGreaterThanEqualAndUserId(to: LocalDate, from: LocalDate, userId: UUID?): List<FoodEntry>

    @Query(value = "SELECT DISTINCT ON (food_id) * FROM food_entry WHERE user_id = :userId AND food_id IS NOT NULL ORDER BY food_id, created_at DESC", nativeQuery = true)
    fun findRecentUniqueFoodEntriesByUser(@Param("userId") userId: UUID): List<FoodEntry>

}