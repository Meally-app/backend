package com.meally.backend.mealType

import org.springframework.stereotype.Service

@Service
class MealTypeService(
    private val mealTypeRepository: MealTypeRepository,
) {
    fun insertMealType(name: String, orderInDay: Int): MealType {
        val mealType = MealType(
            name = name,
            orderInDay = orderInDay,
        )

        return mealTypeRepository.save(mealType)
    }

    fun getAllMealTypes(): List<MealType> {
        return mealTypeRepository.findAll().sortedWith(
            compareBy<MealType> {
                it.orderInDay
            }.thenBy {
                it.name
            }
        )
    }
}