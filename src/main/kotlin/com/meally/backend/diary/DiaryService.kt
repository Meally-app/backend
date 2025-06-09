package com.meally.backend.diary

import com.meally.backend.activity.ActivityEntryRepository
import com.meally.backend.auth.AuthService
import com.meally.backend.common.strava.StravaService
import com.meally.backend.diary.dto.DiaryForDayDto
import com.meally.backend.diary.dto.DiarySummaryDayDto
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.food.FoodEntry
import com.meally.backend.food.FoodEntryRepository
import com.meally.backend.food.getCalories
import com.meally.backend.meal.MealEntry
import com.meally.backend.meal.MealEntryRepository
import com.meally.backend.meal.getCalories
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DiaryService(
    private val authService: AuthService,
    private val foodEntryRepository: FoodEntryRepository,
    private val activityEntryRepository: ActivityEntryRepository,
    private val stravaService: StravaService,
    private val mealEntryRepository: MealEntryRepository,
) {

    suspend fun getDiaryByDate(date: LocalDate): DiaryForDayDto = coroutineScope {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val userId = user.id ?: throw ResourceNotFoundException
        CoroutineScope(Dispatchers.Default).launch {
            stravaService.syncStrava(date, user)
        }
        val food = async { foodEntryRepository.findAllByUserIdAndDate(userId, date) }
        val meals = async { mealEntryRepository.findAllByUserIdAndDate(userId, date) }
        val activity = async { activityEntryRepository.findAllByUserIdAndDate(userId, date) }

        DiaryForDayDto(
            goalCalories = 2000,
            food = food.await(),
            exercise = activity.await(),
            meals = meals.await(),
        )
    }

    fun getDiarySummary(from: LocalDate, to: LocalDate): List<DiarySummaryDayDto> {
        val userId = authService.getLoggedInUser()?.id ?: throw ResourceNotFoundException
        val calorieMap = mutableMapOf<LocalDate, Double>()

        foodEntryRepository
            .findAllByDateLessThanEqualAndDateGreaterThanEqualAndUserId(to, from, userId)
            .forEach { foodEntry ->
                val caloriesForDate = calorieMap[foodEntry.date] ?: 0.0
                calorieMap[foodEntry.date] = caloriesForDate + foodEntry.getCalories()
            }

        mealEntryRepository
            .findAllByDateLessThanEqualAndDateGreaterThanEqualAndUserId(to, from, userId)
            .forEach { mealEntry ->
                val caloriesForDate = calorieMap[mealEntry.date] ?: 0.0
                calorieMap[mealEntry.date] = caloriesForDate + mealEntry.getCalories()
            }

        return calorieMap
            .map {
                DiarySummaryDayDto(
                    date = it.key,
                    calories = it.value,
                )
            }
    }
}