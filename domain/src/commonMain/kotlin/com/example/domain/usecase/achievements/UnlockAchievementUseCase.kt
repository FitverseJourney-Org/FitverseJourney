package com.example.domain.usecase.achievements

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.achievements.AchievementDao

class UnlockAchievementUseCase(
    private val dao: AchievementDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(achievementId: String, unlockedAt: String): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.unlockAchievement(achievementId, userId, unlockedAt)
    }
}
