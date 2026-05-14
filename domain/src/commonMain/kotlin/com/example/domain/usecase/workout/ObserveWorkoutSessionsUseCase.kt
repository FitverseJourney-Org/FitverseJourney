package com.example.domain.usecase.workout

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionDao
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class ObserveWorkoutSessionsUseCase(
    private val dao: WorkoutSessionDao,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<List<WorkoutSessionRecord>> {
        val userId = authRepository.getCurrentUserId() ?: return emptyFlow()
        return dao.observeSessionsByUser(userId)
    }
}
