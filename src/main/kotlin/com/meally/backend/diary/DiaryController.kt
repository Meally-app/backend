package com.meally.backend.diary

import com.meally.backend.diary.dto.DiaryForDayDto
import com.meally.backend.diary.dto.DiarySummaryDayDto
import com.meally.backend.food.FoodEntry
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class DiaryController(
    private val diaryService: DiaryService,
) {

    @GetMapping("/diary")
    fun getFoodDiary(@RequestParam("date") date: LocalDate = LocalDate.now()): ResponseEntity<DiaryForDayDto> {
        return ResponseEntity.ok(runBlocking { diaryService.getDiaryByDate(date) })
    }

    @GetMapping("/diary/summary")
    fun getDiarySummary(
        @RequestParam("from") from: LocalDate,
        @RequestParam("to") to: LocalDate,
    ): ResponseEntity<List<DiarySummaryDayDto>> {
        return ResponseEntity.ok(diaryService.getDiarySummary(from, to))
    }
}