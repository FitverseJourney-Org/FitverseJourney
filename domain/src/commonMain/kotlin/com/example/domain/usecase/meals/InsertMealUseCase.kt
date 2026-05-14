package com.example.domain.usecase.meals

import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao
import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryRecord

class InsertMealUseCase(
    private val dao: MealEntryDao,
) {
    suspend operator fun invoke(meal: MealEntryRecord): Result<Unit> = runCatching {
        dao.insertMeal(meal)
    }
}
