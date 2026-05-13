package com.example.local.datasource.workout

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionDao
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionRecord
import com.journey.database.AppDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WorkoutSessionDaoImpl(database: AppDatabase) : WorkoutSessionDao {

    private val queries = database.appDatabaseQueries

    override fun observeSessionsByUser(userId: String): Flow<List<WorkoutSessionRecord>> =
        queries.selectSessionsByUser(userId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toRecord() } }

    override suspend fun getSessionsByUser(userId: String): List<WorkoutSessionRecord> =
        withContext(Dispatchers.IO) {
            queries.selectSessionsByUser(userId).executeAsList().map { it.toRecord() }
        }

    override suspend fun getSessionsByPeriod(userId: String, from: Long, to: Long): List<WorkoutSessionRecord> =
        withContext(Dispatchers.IO) {
            queries.selectSessionsByUserAndPeriod(userId, from, to).executeAsList().map { it.toRecord() }
        }

    override suspend fun getRecentSessions(userId: String, limit: Long): List<WorkoutSessionRecord> =
        withContext(Dispatchers.IO) {
            queries.selectRecentSessionsByUser(userId, limit).executeAsList().map { it.toRecord() }
        }

    override suspend fun getSessionById(id: String): WorkoutSessionRecord? =
        withContext(Dispatchers.IO) {
            queries.selectSessionById(id).executeAsOneOrNull()?.toRecord()
        }

    override suspend fun insertSession(session: WorkoutSessionRecord): Unit = withContext(Dispatchers.IO) {
        queries.insertSession(
            id              = session.id,
            userId          = session.userId,
            workoutPlanId   = session.workoutPlanId,
            workoutTitle    = session.workoutTitle,
            startedAt       = session.startedAt,
            completedAt     = session.completedAt,
            durationSeconds = session.durationSeconds.toLong(),
            totalVolume     = session.totalVolume,
            totalSets       = session.totalSets.toLong(),
            totalReps       = session.totalReps.toLong(),
            xpEarned        = session.xpEarned.toLong(),
            hasPR           = if (session.hasPR) 1L else 0L,
            intensityLevel  = session.intensityLevel.toLong(),
            muscleGroups    = session.muscleGroups,
            notes           = session.notes,
        )
    }

    override suspend fun completeSession(
        id: String, userId: String, completedAt: Long, durationSeconds: Int,
        totalVolume: Double, totalSets: Int, totalReps: Int, xpEarned: Int,
        hasPR: Boolean, intensityLevel: Int, muscleGroups: String, notes: String,
    ): Unit = withContext(Dispatchers.IO) {
        queries.completeSession(
            completedAt     = completedAt,
            durationSeconds = durationSeconds.toLong(),
            totalVolume     = totalVolume,
            totalSets       = totalSets.toLong(),
            totalReps       = totalReps.toLong(),
            xpEarned        = xpEarned.toLong(),
            hasPR           = if (hasPR) 1L else 0L,
            intensityLevel  = intensityLevel.toLong(),
            muscleGroups    = muscleGroups,
            notes           = notes,
            id              = id,
            userId          = userId,
        )
    }

    override suspend fun deleteSession(id: String, userId: String): Unit =
        withContext(Dispatchers.IO) { queries.deleteSession(id = id, userId = userId) }

    override suspend fun countSessions(userId: String): Long =
        withContext(Dispatchers.IO) { queries.countSessionsByUser(userId).executeAsOne() }

    override suspend fun sumVolume(userId: String): Double =
        withContext(Dispatchers.IO) {
            queries.sumVolumeByUser(userId).executeAsOne() ?: 0.0
        }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun com.journey.database.migrations.WorkoutSessionEntity.toRecord() = WorkoutSessionRecord(
        id              = id,
        userId          = userId,
        workoutPlanId   = workoutPlanId,
        workoutTitle    = workoutTitle,
        startedAt       = startedAt,
        completedAt     = completedAt,
        durationSeconds = durationSeconds.toInt(),
        totalVolume     = totalVolume,
        totalSets       = totalSets.toInt(),
        totalReps       = totalReps.toInt(),
        xpEarned        = xpEarned.toInt(),
        hasPR           = hasPR != 0L,
        intensityLevel  = intensityLevel.toInt(),
        muscleGroups    = muscleGroups,
        notes           = notes,
    )
}
