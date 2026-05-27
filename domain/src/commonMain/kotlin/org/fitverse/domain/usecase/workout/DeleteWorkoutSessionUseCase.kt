package org.fitverse.domain.usecase.workout

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSetDao

class DeleteWorkoutSessionUseCase(
    private val sessionDao: WorkoutSessionDao,
    private val setDao: WorkoutSetDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(sessionId: String): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        setDao.deleteSetsBySession(sessionId)
        sessionDao.deleteSession(sessionId, userId)
    }
}
