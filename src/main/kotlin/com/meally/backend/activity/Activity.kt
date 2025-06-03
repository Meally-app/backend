package com.meally.backend.activity

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.imageResource.ImageResource
import jakarta.persistence.*
import java.util.*

@Entity
data class Activity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val name: String,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "image_id")
    val image: ImageResource? = null,

) : BaseModel()
