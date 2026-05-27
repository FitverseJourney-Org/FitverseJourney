package org.fitverse.domain.usecase.stats

import org.fitverse.domain.repository.dbLocal.sqldelight.stats.UserStatsDao
import org.fitverse.domain.repository.dbLocal.sqldelight.stats.UserStatsRecord

class UpsertUserStatsUseCase(
    private val dao: UserStatsDao,
) {
    suspend operator fun invoke(stats: UserStatsRecord): Result<Unit> = runCatching {
        dao.upsertStats(stats)
    }
}
