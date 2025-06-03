package com.meally.backend.common.openFoodFacts.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.meally.backend.food.Food
import com.meally.backend.imageResource.ImageResource
@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenFoodFactsProductDto(
    val product: ProductDetailsDto,
    val code: String,
)

data class ProductDetailsDto(
    val nutriments: NutrimentsDto,
    @JsonProperty("serving_quantity")
    val servingQuantity: String? = null,
    @JsonProperty("serving_quantity_unit")
    val servingQuantityUnit: String? = null,
    @JsonProperty("product_name")
    val name: String,
    @JsonProperty("product_quantity_unit")
    val unitOfMeasurement: String,
    @JsonProperty("image_url")
    val imageUrl: String,
)

data class NutrimentsDto(
    @JsonProperty("carbohydrates_100g")
    val carbsPer100g: Double,
    @JsonProperty("energy-kcal_100g")
    val energyKcal: Double,
    @JsonProperty("fat_100g")
    val fatPer100g: Double,
    @JsonProperty("proteins_100g")
    val proteinPer100g: Double,
    @JsonProperty("saturated-fat_100g")
    val saturatedFatPer100g: Double,
    @JsonProperty("sugars_100g")
    val sugarPer100g: Double,
)

fun OpenFoodFactsProductDto.toFood() : Food = Food(
    name = product.name,
    barcode = code,
    calories = product.nutriments.energyKcal,
    fat = product.nutriments.fatPer100g,
    saturatedFat = product.nutriments.saturatedFatPer100g,
    carbs = product.nutriments.carbsPer100g,
    sugars = product.nutriments.sugarPer100g,
    protein = product.nutriments.proteinPer100g,
    unitOfMeasurement = Food.UnitOfMeasurement.safeValueOf(product.unitOfMeasurement),
    image = ImageResource(
        name = "${product.name}_image",
        resourceUrl = product.imageUrl,
    )
)

