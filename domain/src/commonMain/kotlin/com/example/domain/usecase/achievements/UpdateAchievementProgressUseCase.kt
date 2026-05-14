package com.example.domain.usecase.achievements

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.achievements.AchievementDao

class UpdateAchievementProgressUseCase(
    private val dao: AchievementDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        achievementId: String,
        progress: Double,
        status: String,
    ): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.updateProgress(achievementId, userId, progress, status)
    }
}
