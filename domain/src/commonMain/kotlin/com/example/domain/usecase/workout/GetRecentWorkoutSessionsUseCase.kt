package org.fitverse.domain.usecase.workout

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionRecord

class GetRecentWorkoutSessionsUseCase(
    private val dao: WorkoutSessionDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(limit: Long = 10): Result<List<WorkoutSessionRecord>> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.getRecentSessions(userId, limit)
    }
}
