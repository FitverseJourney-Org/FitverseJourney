package org.fitverse.domain.usecase.missions

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.catalog.CatalogMissionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord

class SwapMissionUseCase(
    private val missionDao: DailyMissionDao,
    private val catalogDao: CatalogMissionDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        taskToReplaceId: String,
        catalogMissionId: String,
        date: String,
        nowMillis: Long,
    ): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        val catalog = catalogDao.getAll().first { it.id == catalogMissionId }
        missionDao.deleteMission(id = taskToReplaceId, userId = userId)
        missionDao.insertMission(
            DailyMissionRecord(
                id          = "${date}_${userId}_swap_$nowMillis",
                userId      = userId,
                date        = date,
                title       = catalog.title,
                description = catalog.description,
                xpReward    = catalog.xpReward,
                type        = catalog.type,
                isCompleted = false,
                completedAt = null,
            )
        )
    }
}
