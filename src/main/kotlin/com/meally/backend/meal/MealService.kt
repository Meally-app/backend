package com.meally.backend.meal

import com.meally.backend.auth.AuthService
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.food.FoodRepository
import com.meally.backend.meal.dto.BrowseMealDto
import com.meally.backend.meal.dto.InsertMealDto
import com.meally.backend.meal.dto.InsertMealEntryDto
import com.meally.backend.mealType.MealTypeRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class MealService (
    private val mealRepository: MealRepository,
    private val authService: AuthService,
    private val foodRepository: FoodRepository,
    private val mealTypeRepository: MealTypeRepository,
    private val mealEntryRepository: MealEntryRepository,
) {

    fun browseMeals(
        query: String,
        caloriesMin: Double,
        caloriesMax: Double,
    ): List<BrowseMealDto> {
        return mealRepository
            .findAll()
            .asSequence()
            .filter { it.status == MealVisibility.PUBLIC }
            .filter { it.getCalories() in caloriesMin..caloriesMax }
            .filter { it.name.lowercase().contains(query.lowercase()) }
            .take(20)
            .map {
                BrowseMealDto(
                    id = it.id.toString(),
                    name = it.name,
                    user = it.user,
                    calories = it.getCalories(),
                )
            }
            .toList()
    }

    fun insertMeal(dto: InsertMealDto): Meal {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val foodInMeal = mutableListOf<FoodInMeal>()
        val meal = Meal(
            id = dto.mealId,
            name = dto.name,
            status = dto.visibility,
            foodInMeal = foodInMeal,
            user = user,
        )

        val foodList = foodRepository.findAllById(dto.foodInMeal.map { it.foodId })

        dto.foodInMeal.forEach { foodInMealDto ->
            foodList.firstOrNull { foodInMealDto.foodId == it.id }?.let {
                foodInMeal.add(
                    FoodInMeal(
                        meal = meal,
                        food = it,
                        amount = foodInMealDto.amount,
                    )
                )
            }
        }

        return mealRepository.save(meal)
    }

    fun getMealById(mealId: UUID): Meal {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val meal = mealRepository.findById(mealId).getOrNull() ?: throw ResourceNotFoundException
        if (meal.user == user) {
            return meal
        } else throw ResourceNotFoundException
    }

    fun deleteMeal(mealId: UUID) {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val meal = mealRepository.findById(mealId).getOrNull() ?: throw ResourceNotFoundException
        if (meal.user == user) {
            mealRepository.delete(meal)
        } else throw ResourceNotFoundException
    }

    fun createMealEntry(dto: InsertMealEntryDto): MealEntry {
        val meal = mealRepository.findById(dto.mealId).getOrNull() ?: throw ResourceNotFoundException
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val mealType = mealTypeRepository.findByName(dto.mealType) ?: throw ResourceNotFoundException

        val mealEntry = MealEntry(
            user = user,
            meal = meal,
            mealType = mealType,
            amount = dto.amount,
            date = dto.date,
        )

        return mealEntryRepository.save(mealEntry)
    }

    fun getUserMeals(): List<Meal> {
        val userId = authService.getLoggedInUser()?.id ?: throw ResourceNotFoundException
        return mealRepository.findAllByUserId(userId)
    }

}