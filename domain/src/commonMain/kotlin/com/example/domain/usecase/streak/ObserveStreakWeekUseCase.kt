package com.example.domain.usecase.streak

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.streak.StreakDao
import com.example.domain.repository.dbLocal.sqldelight.streak.StreakRecord
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
