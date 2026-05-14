package com.example.domain.usecase.meals

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.nutrition.DailyMacros
import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao

class GetDailyMacrosUseCase(
    private val dao: MealEntryDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(date: String): Result<DailyMacros> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.getDailyMacros(userId, date)
    }
}
