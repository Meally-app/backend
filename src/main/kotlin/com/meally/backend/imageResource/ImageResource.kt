package com.meally.backend.imageResource

import com.meally.backend.common.baseModel.BaseModel
import jakarta.persistence.*
import java.util.*

@Entity
data class ImageResource(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val name: String? = null,

    val colorHexCode: String? = null,

    val resourceUrl: String? = null,

) : BaseModel()
