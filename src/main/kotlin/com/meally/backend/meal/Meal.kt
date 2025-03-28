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

    @ManyToOne
    @JoinColumn(name = "meal_type", nullable = false)
    val mealType: MealType,

    @OneToMany(mappedBy = "meal", cascade = [CascadeType.ALL])
    val foodInMeal: List<FoodInMeal>,

    val status: MealVisibility,

    ) : BaseModel()

enum class MealVisibility {
    PRIVATE, PUBLIC
}
