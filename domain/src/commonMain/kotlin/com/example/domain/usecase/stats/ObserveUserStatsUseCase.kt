package com.example.domain.usecase.stats

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.stats.UserStatsDao
import com.example.domain.repository.dbLocal.sqldelight.stats.UserStatsRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ObserveUserStatsUseCase(
    private val dao: UserStatsDao,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<UserStatsRecord?> {
        val userId = authRepository.getCurrentUserId() ?: return flowOf(null)
        return dao.observeStats(userId)
    }
}
