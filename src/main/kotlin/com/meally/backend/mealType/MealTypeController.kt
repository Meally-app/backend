package com.meally.backend.mealType

import com.meally.backend.mealType.dto.InsertMealTypeDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MealTypeController(
    private val mealTypeService: MealTypeService
) {

    @GetMapping("/public/meal-type")
    fun getMealTypes() = ResponseEntity.ok(mealTypeService.getAllMealTypes())

    @PostMapping("/public/meal-type")
    fun insertMealType(@RequestBody dto: InsertMealTypeDto): ResponseEntity<MealType> {
        return ResponseEntity.ok(mealTypeService.insertMealType(dto.name, dto.orderInDay))
    }
}