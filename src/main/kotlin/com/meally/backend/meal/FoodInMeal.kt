package com.meally.backend.meal

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.food.Food
import jakarta.persistence.*
import java.util.*

@Entity
data class FoodInMeal(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "meal_id", nullable = false)
    val meal: Meal,

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    val food: Food,

    val amount: Double,

) : BaseModel()
