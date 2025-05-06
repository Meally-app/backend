package com.meally.backend.mealType

import org.springframework.data.jpa.repository.JpaRepository

interface MealTypeRepository: JpaRepository<MealType, String> {
    fun findByName(name: String): MealType?

}