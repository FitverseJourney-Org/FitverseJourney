package com.example.domain.usecase.missions

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord
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
