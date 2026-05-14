package com.example.domain.usecase.workout

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionDao
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionRecord

class GetRecentWorkoutSessionsUseCase(
    private val dao: WorkoutSessionDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(limit: Long = 10): Result<List<WorkoutSessionRecord>> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.getRecentSessions(userId, limit)
    }
}
