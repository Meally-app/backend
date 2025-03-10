package com.meally.backend.food

import com.meally.backend.auth.AuthService
import com.meally.backend.common.baseModel.openFoodFacts.OpenFoodFactsService
import com.meally.backend.exception.model.ResourceNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class FoodController(
    private val foodService: FoodService,
    private val authService: AuthService,
    private val openFoodFactsService: OpenFoodFactsService,
) {

    @GetMapping("/food")
    fun getAllFood(): List<Food> {
        println("[TEST] ${authService.getLoggedInUser()}")
        return foodService.getAllFood()
    }

    @GetMapping("/food/{barcode}")
    fun getFoodByBarcode(@PathVariable barcode: String): Food {
        return openFoodFactsService.getFood(barcode) ?: throw ResourceNotFoundException
    }

    @PostMapping("/food")
    fun insertFood(@RequestBody food: Food): Food {
        return foodService.insertFood(food)
    }
}