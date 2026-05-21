package org.fitverse.domain.usecase.streak

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.streak.StreakDao
import org.fitverse.domain.repository.dbLocal.sqldelight.streak.StreakRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ObserveStreakWeekUseCase(
    private val dao: StreakDao,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<List<StreakRecord>> {
        val userId = authRepository.getCurrentUserId() ?: return flowOf(emptyList())
        return dao.observeStreakWeek(userId)
    }
}
