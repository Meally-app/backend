package com.meally.backend.food

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FoodRepository : CrudRepository<Food, String> {
    fun findByBarcode(barcode: String): Food?
}