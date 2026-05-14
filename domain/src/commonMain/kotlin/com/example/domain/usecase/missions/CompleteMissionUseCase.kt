package com.example.domain.usecase.missions

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao

class CompleteMissionUseCase(
    private val dao: DailyMissionDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(missionId: String, completedAt: Long): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.completeMission(missionId, userId, completedAt)
    }
}
