package com.example.domain.usecase.stats

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.stats.UserStatsDao

class UpdateStepsUseCase(
    private val dao: UserStatsDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(steps: Int, now: Long): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.updateSteps(userId, steps, now)
    }
}
