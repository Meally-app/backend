package com.meally.backend.food

import com.meally.backend.auth.AuthService
import com.meally.backend.common.openFoodFacts.OpenFoodFactsService
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.food.dto.DiarySummaryDayDto
import com.meally.backend.food.dto.FoodEntryInsertDto
import org.springframework.data.jpa.repository.Query
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.time.LocalDate

@RestController
class FoodController(
    private val foodService: FoodService,
) {

    @GetMapping("/food")
    fun getAllFood(): List<Food> {
        return foodService.getAllFood()
    }

    @GetMapping("/public/food/search")
    fun searchFood(@RequestParam("query") query: String): ResponseEntity<List<Food>> {
        return ResponseEntity.ok(foodService.searchFood(query));
    }

    @GetMapping("/public/food/{barcode}")
    fun getFoodByBarcodePublic(@PathVariable barcode: String): ResponseEntity<Food> {
        return ResponseEntity.ok(foodService.getFoodByBarcode(barcode))
    }

    @GetMapping("/food/recent")
    fun getRecentFood(): ResponseEntity<List<Food>> {
        return ResponseEntity.ok(foodService.getRecentFood())
    }

    @PostMapping("/food-entry")
    fun insertFoodEntry(@RequestBody dto: FoodEntryInsertDto): ResponseEntity<FoodEntry> {
        return ResponseEntity.ok(foodService.insertFoodEntry(dto))
    }

    @GetMapping("/diary")
    fun getFoodDiary(@RequestParam("date") date: LocalDate = LocalDate.now()): ResponseEntity<List<FoodEntry>> {
        return ResponseEntity.ok(foodService.getDiaryByDate(date))
    }

    @GetMapping("/diary/summary")
    fun getDiarySummary(
        @RequestParam("from") from: LocalDate,
        @RequestParam("to") to: LocalDate,
    ): ResponseEntity<List<DiarySummaryDayDto>> {
        return ResponseEntity.ok(foodService.getDiarySummary(from, to))
    }
}