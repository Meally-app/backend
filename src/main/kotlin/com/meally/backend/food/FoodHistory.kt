package com.meally.backend.food

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.users.User
import jakarta.persistence.*
import java.util.*

@Entity
data class FoodHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    val food: Food,

) : BaseModel()
