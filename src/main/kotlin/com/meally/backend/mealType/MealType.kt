package com.meally.backend.mealType

import com.meally.backend.common.baseModel.BaseModel
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class MealType(
    @Id
    val name: String,
    val orderInDay: Int,
) : BaseModel()
