package com.example.domain.usecase.stats

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.stats.UserStatsDao

class AddXpUseCase(
    private val dao: UserStatsDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(amount: Int, now: Long): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.addXp(userId, amount, now)
    }
}
