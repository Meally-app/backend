package com.meally.backend.meal

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MealRepository : JpaRepository<Meal, UUID> {
    fun findAllByUserId(userId: UUID): List<Meal>
}