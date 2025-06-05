package com.meally.backend.diary

import com.meally.backend.activity.ActivityEntryRepository
import com.meally.backend.auth.AuthService
import com.meally.backend.common.strava.StravaService
import com.meally.backend.diary.dto.DiaryForDayDto
import com.meally.backend.diary.dto.DiarySummaryDayDto
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.food.FoodEntry
import com.meally.backend.food.FoodEntryRepository
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DiaryService(
    private val authService: AuthService,
    private val foodEntryRepository: FoodEntryRepository,
    private val activityEntryRepository: ActivityEntryRepository,
    private val stravaService: StravaService,
) {

    suspend fun getDiaryByDate(date: LocalDate): DiaryForDayDto = coroutineScope {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val userId = user.id ?: throw ResourceNotFoundException
        CoroutineScope(Dispatchers.Default).launch {
            stravaService.syncStrava(date, user)
        }
        val food = async { foodEntryRepository.findAllByUserIdAndDate(userId, date) }
        val activity = async { activityEntryRepository.findAllByUserIdAndDate(userId, date) }

        DiaryForDayDto(
            goalCalories = 2000,
            food = food.await(),
            exercise = activity.await(),
        )
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