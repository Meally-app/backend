package com.meally.backend.weight

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.users.User
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
data class UserWeight(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    val weight: Double,

    val date: LocalDate,

) : BaseModel()
