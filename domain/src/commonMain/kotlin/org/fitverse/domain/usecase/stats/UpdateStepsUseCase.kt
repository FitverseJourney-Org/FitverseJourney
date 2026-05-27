package org.fitverse.domain.usecase.stats

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.stats.UserStatsDao

class UpdateStepsUseCase(
    private val dao: UserStatsDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(steps: Int, now: Long): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.updateSteps(userId, steps, now)
    }
}
