package org.fitverse.domain.repository.dbLocal.sqldelight.streak

import kotlinx.coroutines.flow.Flow

interface StreakDao {
    fun observeStreakWeek(userId: String): Flow<List<StreakRecord>>
    suspend fun getStreakByDate(userId: String, date: String): StreakRecord?
    suspend fun getStreakLastDays(userId: String, fromDate: String): List<StreakRecord>
    suspend fun getCurrentStreak(userId: String): Int
    suspend fun getStreakWeek(userId: String): List<StreakDayRecord>
    suspend fun upsertStreak(record: StreakRecord)
    suspend fun deleteOldRecords(userId: String, beforeDate: String)
}

data class StreakRecord(
    val id: String,
    val userId: String,
    val date: String,
    val isCheckedIn: Boolean,
    val streakCount: Int,
)

data class StreakDayRecord(
    val date: String,
    val isCheckedIn: Boolean,
)
