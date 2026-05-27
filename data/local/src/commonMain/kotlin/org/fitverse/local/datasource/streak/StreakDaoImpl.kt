package org.fitverse.data.local.datasource.streak

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import org.fitverse.domain.repository.dbLocal.sqldelight.streak.StreakDao
import org.fitverse.domain.repository.dbLocal.sqldelight.streak.StreakDayRecord
import org.fitverse.domain.repository.dbLocal.sqldelight.streak.StreakRecord
import com.journey.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StreakDaoImpl(database: AppDatabase) : StreakDao {

    private val queries = database.streakEntityQueries

    override fun observeStreakWeek(userId: String): Flow<List<StreakRecord>> =
        queries.selectStreakLastDays(userId = userId, date = "")
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toRecord() } }

    override suspend fun getStreakByDate(userId: String, date: String): StreakRecord? =
        withContext(Dispatchers.IO) {
            queries.selectStreakByUserAndDate(userId, date).executeAsOneOrNull()?.toRecord()
        }

    override suspend fun getStreakLastDays(userId: String, fromDate: String): List<StreakRecord> =
        withContext(Dispatchers.IO) {
            queries.selectStreakLastDays(userId, fromDate).executeAsList().map { it.toRecord() }
        }

    override suspend fun getCurrentStreak(userId: String): Int =
        withContext(Dispatchers.IO) {
            queries.selectCurrentStreak(userId).executeAsOneOrNull()?.toInt() ?: 0
        }

    override suspend fun getStreakWeek(userId: String): List<StreakDayRecord> =
        withContext(Dispatchers.IO) {
            queries.selectStreakWeek(userId).executeAsList().map {
                StreakDayRecord(date = it.date, isCheckedIn = it.isCheckedIn != 0L)
            }
        }

    override suspend fun upsertStreak(record: StreakRecord): Unit = withContext(Dispatchers.IO) {
        queries.upsertStreak(
            id          = record.id,
            userId      = record.userId,
            date        = record.date,
            isCheckedIn = if (record.isCheckedIn) 1L else 0L,
            streakCount = record.streakCount.toLong(),
        )
    }

    override suspend fun deleteOldRecords(userId: String, beforeDate: String): Unit =
        withContext(Dispatchers.IO) { queries.deleteStreaksOlderThan(userId = userId, date = beforeDate) }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun com.journey.streak.StreakEntity.toRecord() = StreakRecord(
        id          = id,
        userId      = userId,
        date        = date,
        isCheckedIn = isCheckedIn != 0L,
        streakCount = streakCount.toInt(),
    )
}
