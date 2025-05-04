package com.meally.backend.users

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.imageResource.ImageResource
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "meally_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val externalId: String, // id we receive from firebase

    @Column(unique = true)
    val username: String,

    @Column(unique = true)
    val email: String,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "image_id")
    val profileImage: ImageResource? = null,

) : BaseModel()
