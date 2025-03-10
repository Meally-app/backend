package com.meally.backend.users

import com.meally.backend.common.baseModel.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "meally_user")
data class User(
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID? = null,
    val externalId: String, // id we receive from firebase

    @Column(unique = true)
    val username: String,

    @Column(unique = true)
    val email: String,

    val profilePicUrl: String?,
) : BaseModel()
