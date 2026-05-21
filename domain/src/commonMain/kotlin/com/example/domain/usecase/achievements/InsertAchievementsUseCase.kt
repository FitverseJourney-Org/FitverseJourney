package org.fitverse.domain.usecase.achievements

import org.fitverse.domain.repository.dbLocal.sqldelight.achievements.AchievementDao
import org.fitverse.domain.repository.dbLocal.sqldelight.achievements.AchievementRecord

class InsertAchievementsUseCase(
    private val dao: AchievementDao,
) {
    suspend operator fun invoke(achievements: List<AchievementRecord>): Result<Unit> = runCatching {
        dao.insertAchievements(achievements)
    }
}
