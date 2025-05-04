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
) {

    @GetMapping("/food")
    fun getAllFood(): List<Food> {
        return foodService.getAllFood()
    }

    @GetMapping("/public/food/{barcode}")
    fun getFoodByBarcodePublic(@PathVariable barcode: String): Food {
        return foodService.getFoodByBarcode(barcode)
    }

}