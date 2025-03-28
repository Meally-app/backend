package com.meally.backend.meal

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.users.User
import jakarta.persistence.*
import java.util.*

@Entity
data class MealLike(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    val meal: Meal,
) : BaseModel()
