package com.example.domain.usecase.missions

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.catalog.CatalogMissionDao
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord

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
