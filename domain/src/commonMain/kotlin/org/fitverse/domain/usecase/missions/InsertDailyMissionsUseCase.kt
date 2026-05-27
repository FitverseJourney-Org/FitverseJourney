package org.fitverse.domain.usecase.missions

import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord

class InsertDailyMissionsUseCase(
    private val dao: DailyMissionDao,
) {
    suspend operator fun invoke(missions: List<DailyMissionRecord>): Result<Unit> = runCatching {
        dao.insertMissions(missions)
    }
}
