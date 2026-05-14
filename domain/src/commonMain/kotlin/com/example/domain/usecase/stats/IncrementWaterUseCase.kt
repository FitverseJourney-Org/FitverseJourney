package com.example.domain.usecase.stats

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.stats.UserStatsDao

class IncrementWaterUseCase(
    private val dao: UserStatsDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(now: Long): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.incrementWater(userId, now)
    }
}
