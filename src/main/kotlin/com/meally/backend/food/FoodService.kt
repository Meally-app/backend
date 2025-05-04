package com.meally.backend.food

import com.meally.backend.common.baseModel.openFoodFacts.OpenFoodFactsService
import com.meally.backend.exception.model.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class FoodService(
    private val foodRepository: FoodRepository,
    private val openFoodFactsService: OpenFoodFactsService,
) {
    fun getAllFood() = foodRepository.findAll().toList()

    fun getFoodByBarcode(barcode: String): Food {
        val food = foodRepository.findByBarcode(barcode)
        if (food != null) return food

        val foodFromApi = openFoodFactsService.getFood(barcode) ?: throw ResourceNotFoundException

        return foodRepository.save(foodFromApi)
    }
}