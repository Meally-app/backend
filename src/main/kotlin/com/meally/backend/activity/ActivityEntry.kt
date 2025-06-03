package com.meally.backend.activity

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.food.Food
import com.meally.backend.users.User
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
data class ActivityEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val name: String,

    val date: LocalDate,

    val caloriesBurnt: Double,

    val timeSpent: Long,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    val activity: Activity,

) : BaseModel()
