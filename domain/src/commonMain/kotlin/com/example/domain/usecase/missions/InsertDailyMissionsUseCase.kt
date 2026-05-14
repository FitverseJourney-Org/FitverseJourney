package com.example.domain.usecase.missions

import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord

class InsertDailyMissionsUseCase(
    private val dao: DailyMissionDao,
) {
    suspend operator fun invoke(missions: List<DailyMissionRecord>): Result<Unit> = runCatching {
        dao.insertMissions(missions)
    }
}
