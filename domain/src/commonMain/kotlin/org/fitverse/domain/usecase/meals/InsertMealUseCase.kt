package org.fitverse.domain.usecase.meals

import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryRecord

class InsertMealUseCase(
    private val dao: MealEntryDao,
) {
    suspend operator fun invoke(meal: MealEntryRecord): Result<Unit> = runCatching {
        dao.insertMeal(meal)
    }
}
