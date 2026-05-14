package com.example.domain.usecase.workout

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionDao
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionRecord

class GetSessionsByPeriodUseCase(
    private val dao: WorkoutSessionDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(from: Long, to: Long): Result<List<WorkoutSessionRecord>> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.getSessionsByPeriod(userId, from, to)
    }
}
