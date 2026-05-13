package com.example.local.datasource.stats

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.domain.repository.dbLocal.sqldelight.stats.UserStatsDao
import com.example.domain.repository.dbLocal.sqldelight.stats.UserStatsRecord
import com.journey.database.AppDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserStatsDaoImpl(database: AppDatabase) : UserStatsDao {

    private val queries = database.appDatabaseQueries

    override fun observeStats(userId: String): Flow<UserStatsRecord?> =
        queries.selectStatsByUser(userId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { it?.toRecord() }

    override suspend fun getStats(userId: String): UserStatsRecord? =
        withContext(Dispatchers.IO) {
            queries.selectStatsByUser(userId).executeAsOneOrNull()?.toRecord()
        }

    override suspend fun upsertStats(stats: UserStatsRecord): Unit = withContext(Dispatchers.IO) {
        queries.upsertStats(
            userId        = stats.userId,
            currentXp     = stats.currentXp.toLong(),
            currentLevel  = stats.currentLevel.toLong(),
            totalXp       = stats.totalXp.toLong(),
            stepsToday    = stats.stepsToday.toLong(),
            stepsGoal     = stats.stepsGoal.toLong(),
            waterGlasses  = stats.waterGlasses.toLong(),
            waterGoal     = stats.waterGoal.toLong(),
            currentStreak = stats.currentStreak.toLong(),
            longestStreak = stats.longestStreak.toLong(),
            totalWorkouts = stats.totalWorkouts.toLong(),
            totalPRs      = stats.totalPRs.toLong(),
            updatedAt     = stats.updatedAt,
        )
    }

    override suspend fun addXp(userId: String, xp: Int, now: Long): Unit =
        withContext(Dispatchers.IO) { queries.addXp(xp.toLong(), xp.toLong(), now, userId) }

    override suspend fun incrementWater(userId: String, now: Long): Unit =
        withContext(Dispatchers.IO) { queries.incrementWater(now, userId) }

    override suspend fun resetDailyStats(userId: String, now: Long): Unit =
        withContext(Dispatchers.IO) { queries.resetDailyStats(now, userId) }

    override suspend fun updateSteps(userId: String, steps: Int, now: Long): Unit =
        withContext(Dispatchers.IO) { queries.updateSteps(steps.toLong(), now, userId) }

    override suspend fun incrementWorkouts(userId: String, now: Long): Unit =
        withContext(Dispatchers.IO) { queries.incrementWorkouts(now, userId) }

    override suspend fun incrementPRs(userId: String, count: Int, now: Long): Unit =
        withContext(Dispatchers.IO) { queries.incrementPRs(count.toLong(), now, userId) }

    override suspend fun updateStreak(userId: String, current: Int, longest: Int, now: Long): Unit =
        withContext(Dispatchers.IO) {
            queries.updateStreak(current.toLong(), longest.toLong(), now, userId)
        }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun com.journey.database.migrations.UserStatsEntity.toRecord() = UserStatsRecord(
        userId        = userId,
        currentXp     = currentXp.toInt(),
        currentLevel  = currentLevel.toInt(),
        totalXp       = totalXp.toInt(),
        stepsToday    = stepsToday.toInt(),
        stepsGoal     = stepsGoal.toInt(),
        waterGlasses  = waterGlasses.toInt(),
        waterGoal     = waterGoal.toInt(),
        currentStreak = currentStreak.toInt(),
        longestStreak = longestStreak.toInt(),
        totalWorkouts = totalWorkouts.toInt(),
        totalPRs      = totalPRs.toInt(),
        updatedAt     = updatedAt,
    )
}
