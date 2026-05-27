package org.fitverse.local.datasource.catalog

import org.fitverse.domain.repository.dbLocal.sqldelight.catalog.CatalogMissionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.catalog.CatalogMissionRecord
import com.journey.AppDatabase
import com.journey.mission.CatalogMissionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class CatalogMissionDaoImpl(database: AppDatabase) : CatalogMissionDao {

    private val queries = database.catalogMissionEntityQueries

    override suspend fun getAll(): List<CatalogMissionRecord> =
        withContext(Dispatchers.IO) {
            queries.selectAllCatalogMissions().executeAsList().map { it.toRecord() }
        }

    override suspend fun count(): Long =
        withContext(Dispatchers.IO) {
            queries.countCatalogMissions().executeAsOne()
        }

    override suspend fun insertAll(missions: List<CatalogMissionRecord>): Unit =
        withContext(Dispatchers.IO) {
            missions.forEach { m ->
                queries.insertCatalogMission(
                    id          = m.id,
                    title       = m.title,
                    description = m.description,
                    xpReward    = m.xpReward.toLong(),
                    type        = m.type,
                    isSpecial   = if (m.isSpecial) 1L else 0L,
                )
            }
        }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun CatalogMissionEntity.toRecord() =
        CatalogMissionRecord(
            id          = id,
            title       = title,
            description = description,
            xpReward    = xpReward.toInt(),
            type        = type,
            isSpecial   = isSpecial != 0L,
        )
}
