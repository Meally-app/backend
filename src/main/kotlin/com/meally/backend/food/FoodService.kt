package com.meally.backend.food

import org.springframework.stereotype.Service

@Service
class FoodService(
    private val foodRepository: FoodRepository,
) {
    fun getAllFood() = foodRepository.findAll().toList()

    fun insertFood(food: Food) = foodRepository.save(food)
}