package org.fitverse.domain.usecase.meals

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryRecord

class CreateMealUseCase(
    private val dao: MealEntryDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(name: String, date: String, nowMillis: Long): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")

        dao.insertMeal(
            MealEntryRecord(
                id           = "${date}_${userId}_meal_$nowMillis",
                userId       = userId,
                date         = date,
                mealType     = "CUSTOM",
                name         = name.trim(),
                totalKcal    = 0,
                totalProtein = 0.0,
                totalCarbs   = 0.0,
                totalFat     = 0.0,
                loggedAt     = nowMillis,
            )
        )
    }
}
