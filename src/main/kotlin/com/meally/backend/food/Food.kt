package com.meally.backend.food

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.imageResource.ImageResource
import jakarta.persistence.*
import java.util.UUID

/*
    Represent a Food object
    if not specified the default unit of measurements is grams per 100g
        e.g. fat = 8.3 means there is 8.3 grams of fat per 100g of the food
 */
@Entity
data class Food (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val name: String,
    val barcode: String,
    val calories: Double,
    val fat: Double,
    val saturatedFat: Double?,
    val carbs: Double,
    val sugars: Double?,
    val protein: Double,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "image_id")
    val image: ImageResource? = null,

) : BaseModel()
