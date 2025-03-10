package com.meally.backend.food

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.mealType.MealType
import com.meally.backend.users.User
import jakarta.persistence.*
import java.util.*

@Entity
data class FoodEntry(
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    val food: Food,

    @ManyToOne
    @JoinColumn(name = "meal_type_id", nullable = false)
    val mealType: MealType,

    val amountInGrams: Double,

    val quantity: Double,

) : BaseModel()
