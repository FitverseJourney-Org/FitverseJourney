package com.example.domain.usecase.achievements

import com.example.domain.repository.dbLocal.sqldelight.achievements.AchievementDao
import com.example.domain.repository.dbLocal.sqldelight.achievements.AchievementRecord

class InsertAchievementsUseCase(
    private val dao: AchievementDao,
) {
    suspend operator fun invoke(achievements: List<AchievementRecord>): Result<Unit> = runCatching {
        dao.insertAchievements(achievements)
    }
}
