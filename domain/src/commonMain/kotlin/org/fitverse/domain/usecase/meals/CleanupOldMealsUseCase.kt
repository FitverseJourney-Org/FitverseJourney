package org.fitverse.domain.usecase.meals

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemDao
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao

class CleanupOldMealsUseCase(
    private val mealDao: MealEntryDao,
    private val foodDao: FoodItemDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(today: String): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: return@runCatching
        // Delete foods before deleting meals (referential safety)
        foodDao.deleteFoodsByUserBeforeDate(userId, today)
        mealDao.deleteMealsByUserBeforeDate(userId, today)
    }
}
