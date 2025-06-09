package com.meally.backend.meal

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.mealType.MealType
import com.meally.backend.users.User
import jakarta.persistence.*
import java.util.*
import kotlin.collections.List

@Entity
data class Meal(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val name: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToMany(mappedBy = "meal", cascade = [CascadeType.ALL], orphanRemoval = true)
    val foodInMeal: List<FoodInMeal>,

    @Enumerated(value = EnumType.STRING)
    val status: MealVisibility,

) : BaseModel()

fun Meal.getCalories() = foodInMeal.sumOf { it.amount * it.food.calories / 100 }

enum class MealVisibility {
    PRIVATE, PUBLIC
}
