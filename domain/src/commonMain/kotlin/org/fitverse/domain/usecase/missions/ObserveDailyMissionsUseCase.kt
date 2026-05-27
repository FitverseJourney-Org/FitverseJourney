package org.fitverse.domain.usecase.missions

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ObserveDailyMissionsUseCase(
    private val dao: DailyMissionDao,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(date: String): Flow<List<DailyMissionRecord>> {
        val userId = authRepository.getCurrentUserId() ?: return flowOf(emptyList())
        return dao.observeMissionsByDate(userId, date)
    }
}
