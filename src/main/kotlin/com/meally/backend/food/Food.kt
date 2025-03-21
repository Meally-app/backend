package com.meally.backend.food

import com.meally.backend.common.baseModel.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.UUID

/*
    Represent a Food object
    if not specified the default unit of measurements is grams per 100g
        e.g. fat = 8.3 means there is 8.3 grams of fat per 100g of the food
 */
@Entity
data class Food (
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID? = null,
    val name: String,
    val barcode: String,
    val imageUrl: String?,
    val calories: Double,
    val fat: Double,
    val saturatedFat: Double?,
    val carbs: Double,
    val sugars: Double?,
    val protein: Double,
) : BaseModel()
