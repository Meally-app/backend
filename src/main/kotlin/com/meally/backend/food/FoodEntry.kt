package com.meally.backend.food

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.mealType.MealType
import com.meally.backend.users.User
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
data class FoodEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = true)
    val food: Food?,

    @ManyToOne
    @JoinColumn(name = "meal_type_id", nullable = false)
    val mealType: MealType,

    val amount: Double,

    val date: LocalDate,

) : BaseModel()
