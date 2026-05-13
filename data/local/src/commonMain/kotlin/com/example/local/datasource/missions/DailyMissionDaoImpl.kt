package com.example.local.datasource.missions

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord
import com.journey.database.AppDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DailyMissionDaoImpl(database: AppDatabase) : DailyMissionDao {

    private val queries = database.appDatabaseQueries

    override fun observeMissionsByDate(userId: String, date: String): Flow<List<DailyMissionRecord>> =
        queries.selectMissionsByUserAndDate(userId, date)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toRecord() } }

    override suspend fun getMissionsByDate(userId: String, date: String): List<DailyMissionRecord> =
        withContext(Dispatchers.IO) {
            queries.selectMissionsByUserAndDate(userId, date).executeAsList().map { it.toRecord() }
        }

    override suspend fun insertMission(mission: DailyMissionRecord): Unit = withContext(Dispatchers.IO) {
        queries.insertMission(
            id          = mission.id,
            userId      = mission.userId,
            date        = mission.date,
            title       = mission.title,
            description = mission.description,
            xpReward    = mission.xpReward.toLong(),
            type        = mission.type,
            isCompleted = if (mission.isCompleted) 1L else 0L,
            completedAt = mission.completedAt,
        )
    }

    override suspend fun insertMissions(missions: List<DailyMissionRecord>): Unit =
        withContext(Dispatchers.IO) { missions.forEach { insertMission(it) } }

    override suspend fun completeMission(id: String, userId: String, completedAt: Long): Unit =
        withContext(Dispatchers.IO) {
            queries.completeMission(completedAt = completedAt, id = id, userId = userId)
        }

    override suspend fun resetMissionsForDate(userId: String, date: String): Unit =
        withContext(Dispatchers.IO) { queries.resetMissionsForDate(userId = userId, date = date) }

    override suspend fun deleteMissionsOlderThan(userId: String, beforeDate: String): Unit =
        withContext(Dispatchers.IO) { queries.deleteMissionsOlderThan(userId = userId, date = beforeDate) }

    override suspend fun countCompleted(userId: String, date: String): Long =
        withContext(Dispatchers.IO) {
            queries.countCompletedByUserAndDate(userId = userId, date = date).executeAsOne()
        }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun com.journey.database.migrations.DailyMissionEntity.toRecord() = DailyMissionRecord(
        id          = id,
        userId      = userId,
        date        = date,
        title       = title,
        description = description,
        xpReward    = xpReward.toInt(),
        type        = type,
        isCompleted = isCompleted != 0L,
        completedAt = completedAt,
    )
}
