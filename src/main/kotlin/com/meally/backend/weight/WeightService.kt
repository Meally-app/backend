package com.meally.backend.weight

import com.meally.backend.auth.AuthService
import com.meally.backend.exception.model.ResourceNotFoundException
import com.meally.backend.weight.dto.WeightDto
import com.meally.backend.weight.dto.toDto
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WeightService(
    private val weightRepository: WeightRepository,
    private val authService: AuthService,
) {
    fun getUserWeights(from: LocalDate, to: LocalDate): List<WeightDto> {
        val userId = authService.getLoggedInUser()?.id ?: throw ResourceNotFoundException
        // both dates inclusive
        return weightRepository.findAllByDateLessThanEqualAndDateGreaterThanEqualAndUserId(to, from, userId).map { it.toDto() }
    }

    fun insertWeight(dto: WeightDto): WeightDto {
        val user = authService.getLoggedInUser() ?: throw ResourceNotFoundException
        val weight = weightRepository.findByDate(dto.date)
        return weightRepository.save(
            UserWeight(
                id = weight?.id,
                user = user,
                weight = dto.weight,
                date = dto.date,
            )
        ).toDto()
    }
}