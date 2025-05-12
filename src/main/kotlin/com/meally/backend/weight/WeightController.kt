package com.meally.backend.weight

import com.meally.backend.weight.dto.WeightDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class WeightController(
    private val weightService: WeightService,
) {
    @GetMapping("/weight")
    fun getUserWeights(
        @RequestParam from: LocalDate = LocalDate.now().minusDays(14),
        @RequestParam to: LocalDate = LocalDate.now(),
    ): ResponseEntity<List<WeightDto>> {
        return ResponseEntity.ok(weightService.getUserWeights(from, to))
    }

    @PostMapping("/weight")
    fun insertWeight(
        @RequestBody weightDto: WeightDto,
    ): ResponseEntity<WeightDto> {
        return ResponseEntity.ok(weightService.insertWeight(weightDto))
    }
}