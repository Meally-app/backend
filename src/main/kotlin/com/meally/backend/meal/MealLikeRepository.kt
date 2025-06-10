package com.meally.backend.meal

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MealLikeRepository : JpaRepository<MealLike, UUID> {

    fun findAllByUserId(userId: UUID): List<MealLike>
}