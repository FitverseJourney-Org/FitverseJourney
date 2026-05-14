package com.example.domain.usecase.workout

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionDao
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSetDao

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
