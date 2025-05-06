package com.meally.backend.food

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FoodRepository : JpaRepository<Food, UUID> {
    fun findByBarcode(barcode: String): Food?
}