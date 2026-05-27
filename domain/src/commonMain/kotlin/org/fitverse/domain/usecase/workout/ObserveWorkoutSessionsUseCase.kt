package org.fitverse.domain.usecase.workout

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionRecord
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
