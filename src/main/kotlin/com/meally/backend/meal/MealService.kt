package com.meally.backend.meal

import com.meally.backend.auth.AuthService
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.food.FoodRepository
import com.meally.backend.meal.dto.*
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
    private val mealLikeRepository: MealLikeRepository,
) {

    fun browseMeals(
        query: String,
        caloriesMin: Double,
        caloriesMax: Double,
        onlyFavorites: Boolean,
    ): List<BrowseMealDto> {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException

        val likedMeals = if (onlyFavorites) {
            mealLikeRepository.findAllByUserId(user.id!!).map { it.meal }
        } else listOf()

        return mealRepository
            .findAll()
            .asSequence()
            .filter { it.status == MealVisibility.PUBLIC }
            .filter { it.getCalories() in caloriesMin..caloriesMax }
            .filter { it.name.lowercase().contains(query.lowercase()) }
            .filter { !onlyFavorites || likedMeals.contains(it) }
            .take(20)
            .map {
                BrowseMealDto(
                    id = it.id.toString(),
                    name = it.name,
                    user = it.user,
                    calories = it.getCalories(),
                    isLiked = likedMeals.contains(it),
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

    fun getMealById(mealId: UUID): MealDetailsDto {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val meal = mealRepository.findById(mealId).getOrNull() ?: throw ResourceNotFoundException
        val likedMeals = mealLikeRepository.findAllByUserId(user.id!!).map { it.meal }
        if (meal.status == MealVisibility.PUBLIC || meal.user == user) {
            return meal.toDto(likedMeals.contains(meal))
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

    fun getUserMeals(): List<MealDetailsDto> {
        val userId = authService.getLoggedInUser()?.id ?: throw ResourceNotFoundException
        val likedMeals = mealLikeRepository.findAllByUserId(userId).map { it.meal }
        return mealRepository.findAllByUserId(userId).map { it.toDto(likedMeals.contains(it)) }
    }

    fun likeMeal(mealId: UUID) {
        val meal = mealRepository.findById(mealId).getOrNull() ?: throw ResourceNotFoundException
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException

        if (meal.user.id == user.id) return

        val alreadyLiked = mealLikeRepository.findAllByUserId(user.id!!).firstOrNull { it.meal.id == meal.id }

        if (alreadyLiked != null) {
            mealLikeRepository.delete(alreadyLiked)
        } else {
            mealLikeRepository.save(
                MealLike(
                    meal = meal,
                    user = user,
                )
            )
        }

    }

}