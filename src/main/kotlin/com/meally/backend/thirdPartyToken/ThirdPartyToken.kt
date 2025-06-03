package com.meally.backend.thirdPartyToken

import com.meally.backend.users.User
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
data class ThirdPartyToken(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    val provider: ThirdPartyTokenProvider,

    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Instant,
)