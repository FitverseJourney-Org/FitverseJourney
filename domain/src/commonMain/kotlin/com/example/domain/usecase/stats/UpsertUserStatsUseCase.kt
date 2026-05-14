package com.example.domain.usecase.stats

import com.example.domain.repository.dbLocal.sqldelight.stats.UserStatsDao
import com.example.domain.repository.dbLocal.sqldelight.stats.UserStatsRecord

class UpsertUserStatsUseCase(
    private val dao: UserStatsDao,
) {
    suspend operator fun invoke(stats: UserStatsRecord): Result<Unit> = runCatching {
        dao.upsertStats(stats)
    }
}
