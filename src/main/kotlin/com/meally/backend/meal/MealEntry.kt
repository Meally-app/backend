package com.meally.backend.meal

import com.meally.backend.mealType.MealType
import com.meally.backend.users.User
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
data class MealEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = true)
    val meal: Meal,

    @ManyToOne
    @JoinColumn(name = "meal_type_id", nullable = false)
    val mealType: MealType,

    val amount: Double,

    val date: LocalDate,
)

fun MealEntry.getCalories(): Double = amount * meal.foodInMeal.sumOf { it.food.calories * it.amount / 100 }

