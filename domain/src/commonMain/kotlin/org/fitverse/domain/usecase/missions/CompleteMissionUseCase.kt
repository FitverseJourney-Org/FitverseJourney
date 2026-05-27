package org.fitverse.domain.usecase.missions

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao

class CompleteMissionUseCase(
    private val dao: DailyMissionDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(missionId: String, completedAt: Long): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.completeMission(missionId, userId, completedAt)
    }
}
