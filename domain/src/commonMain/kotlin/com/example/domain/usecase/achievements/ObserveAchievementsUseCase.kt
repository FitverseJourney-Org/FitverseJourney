package com.example.domain.usecase.achievements

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.achievements.AchievementDao
import com.example.domain.repository.dbLocal.sqldelight.achievements.AchievementRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class ObserveAchievementsUseCase(
    private val dao: AchievementDao,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<List<AchievementRecord>> {
        val userId = authRepository.getCurrentUserId() ?: return emptyFlow()
        return dao.observeAchievementsByUser(userId)
    }
}
