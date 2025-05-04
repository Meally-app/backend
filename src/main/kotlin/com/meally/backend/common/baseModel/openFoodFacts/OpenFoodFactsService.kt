package com.meally.backend.common.baseModel.openFoodFacts

import com.meally.backend.common.baseModel.openFoodFacts.dto.OpenFoodFactsProductDto
import com.meally.backend.common.baseModel.openFoodFacts.dto.toFood
import com.meally.backend.food.Food
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient


@Service
class OpenFoodFactsService {

    private val logger: Logger = LoggerFactory.getLogger(OpenFoodFactsService::class.java)

    private val restClient = RestClient.builder()
        .baseUrl("https://world.openfoodfacts.org/api/v0")
        .build()

    fun getFood(barcode: String): Food? {
        val result = runCatching {
            restClient.get()
                .uri("/product/$barcode")
                .retrieve()
                .body(OpenFoodFactsProductDto::class.java)
                ?.toFood()
        }

        if (result.isFailure) {
            val error = result.exceptionOrNull()
            logger.info("Error: ${error.toString()}")
        }

        return result.getOrNull()
    }

}
