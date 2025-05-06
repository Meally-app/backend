package com.meally.backend.mealType

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.imageResource.ImageResource
import jakarta.persistence.*

@Entity
data class MealType(
    @Id
    val name: String,
    val orderInDay: Int,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "image_id")
    val image: ImageResource? = null,
) : BaseModel()
