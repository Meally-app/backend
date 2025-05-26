package com.meally.backend.food

import com.meally.backend.auth.AuthService
import com.meally.backend.common.baseModel.openFoodFacts.OpenFoodFactsService
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.food.dto.DiarySummaryDayDto
import com.meally.backend.food.dto.FoodEntryInsertDto
import com.meally.backend.mealType.MealTypeRepository
import org.springframework.cglib.core.Local
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class FoodService(
    private val foodRepository: FoodRepository,
    private val openFoodFactsService: OpenFoodFactsService,
    private val authService: AuthService,
    private val mealTypeRepository: MealTypeRepository,
    private val foodEntryRepository: FoodEntryRepository,
) {
    fun getAllFood() = foodRepository.findAll().toList()

    fun getFoodByBarcode(barcode: String): Food {
        val food = foodRepository.findByBarcode(barcode)
        if (food != null) return food

        val foodFromApi = openFoodFactsService.getFood(barcode) ?: throw ResourceNotFoundException

        return foodRepository.save(foodFromApi)
    }

    fun searchFood(query: String): List<Food> {
        return foodRepository.searchByNameContaining(query, PageRequest.of(0, 10));
    }

    fun insertFoodEntry(dto: FoodEntryInsertDto) : FoodEntry {
        val food = dto.foodId?.let { foodRepository.findById(UUID.fromString(it)).getOrNull() }
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val mealType = mealTypeRepository.findByName(dto.mealType) ?: throw ResourceNotFoundException

        val foodEntry = FoodEntry(
            user = user,
            food = food,
            mealType = mealType,
            amount = dto.amount,
            date = dto.date,
        )

        return foodEntryRepository.save(foodEntry)
    }

    fun getDiaryByDate(date: LocalDate): List<FoodEntry> {
        val userId = authService.getLoggedInUser()?.id ?: throw ResourceNotFoundException
        return foodEntryRepository.findAllByUserIdAndDate(userId, date)
    }

    fun getDiarySummary(from: LocalDate, to: LocalDate): List<DiarySummaryDayDto> {
        val userId = authService.getLoggedInUser()?.id ?: throw ResourceNotFoundException
        val foodList = foodEntryRepository.findAllByDateLessThanEqualAndDateGreaterThanEqualAndUserId(to, from, userId)

        return foodList
            .groupBy { it.date }
            .mapValues { (_, entriesOnDate) -> entriesOnDate.sumOf {
                it.food?.let { food ->
                    food.calories * it.amount / 100
                } ?: it.amount
            } }
            .map { DiarySummaryDayDto(
                date = it.key,
                calories = it.value,
            ) }
    }
}