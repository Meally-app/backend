package com.meally.backend.food

import com.meally.backend.common.baseModel.BaseModel
import com.meally.backend.imageResource.ImageResource
import jakarta.persistence.*
import java.util.UUID

@Entity
data class Food (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val name: String,
    val barcode: String,
    val calories: Double,
    val fat: Double,
    val saturatedFat: Double?,
    val carbs: Double,
    val sugars: Double?,
    val protein: Double,
    val unitOfMeasurement: UnitOfMeasurement,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "image_id")
    val image: ImageResource? = null,

) : BaseModel() {
    enum class UnitOfMeasurement(val abbreviation: String,) {
        GRAMS("g"), MILLILITERS("ml"), PIECES("pc");

        companion object {
            fun safeValueOf(value: String) = when (value) {
                "ml" -> MILLILITERS
                else -> GRAMS
            }
        }
    }
}
