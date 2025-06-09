package com.meally.backend.meal

import com.meally.backend.meal.dto.BrowseMealDto
import com.meally.backend.meal.dto.InsertMealDto
import com.meally.backend.meal.dto.InsertMealEntryDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class MealController(
    private val mealService: MealService,
) {

    @GetMapping("/public/meals")
    fun getAllMeals(
        @RequestParam("query") query: String = "",
        @RequestParam("caloriesMax") caloriesMax: Double = Double.MAX_VALUE,
        @RequestParam("caloriesMin") caloriesMin: Double = Double.MIN_VALUE,
    ): ResponseEntity<List<BrowseMealDto>> {
        return ResponseEntity.ok(mealService.browseMeals(query, caloriesMin, caloriesMax))
    }

    @GetMapping("/meals")
    fun getUserMeals() : ResponseEntity<List<Meal>> {
        return ResponseEntity.ok(mealService.getUserMeals())
    }

    @GetMapping("/meals/{id}")
    fun getMealById(@PathVariable("id") mealId: UUID): ResponseEntity<Meal> {
        return ResponseEntity.ok(mealService.getMealById(mealId))
    }

    @PostMapping("/meals")
    fun insertMeal(@RequestBody dto: InsertMealDto): ResponseEntity<Meal> {
        return ResponseEntity.ok(mealService.insertMeal(dto))
    }

    @DeleteMapping("/meals/{id}")
    fun deleteMeal(@PathVariable("id") mealId: UUID): ResponseEntity<Unit> {
        return ResponseEntity.ok(mealService.deleteMeal(mealId))
    }

    @PostMapping("/meal-entry")
    fun insertMealEntry(@RequestBody dto: InsertMealEntryDto): ResponseEntity<MealEntry> {
        return ResponseEntity.ok(mealService.createMealEntry(dto))
    }
}