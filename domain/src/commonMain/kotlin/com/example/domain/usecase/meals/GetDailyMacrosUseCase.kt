package org.fitverse.domain.usecase.meals

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.DailyMacros
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao

class GetDailyMacrosUseCase(
    private val dao: MealEntryDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(date: String): Result<DailyMacros> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.getDailyMacros(userId, date)
    }
}
