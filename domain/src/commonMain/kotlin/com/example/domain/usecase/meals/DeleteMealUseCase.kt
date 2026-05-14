package com.example.domain.usecase.meals

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao

class DeleteMealUseCase(
    private val dao: MealEntryDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(mealId: String): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.deleteMeal(mealId, userId)
    }
}
