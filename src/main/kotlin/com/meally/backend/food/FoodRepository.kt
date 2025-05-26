package com.meally.backend.food

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FoodRepository : JpaRepository<Food, UUID> {
    fun findByBarcode(barcode: String): Food?

    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    fun searchByNameContaining(@Param("query") query: String, pageable: Pageable): List<Food>
}