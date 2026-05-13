package com.example.domain.repository.dbLocal.sqldelight.stats

import kotlinx.coroutines.flow.Flow

interface UserStatsDao {
    fun observeStats(userId: String): Flow<UserStatsRecord?>
    suspend fun getStats(userId: String): UserStatsRecord?
    suspend fun upsertStats(stats: UserStatsRecord)
    suspend fun addXp(userId: String, xp: Int, now: Long)
    suspend fun incrementWater(userId: String, now: Long)
    suspend fun resetDailyStats(userId: String, now: Long)
    suspend fun updateSteps(userId: String, steps: Int, now: Long)
    suspend fun incrementWorkouts(userId: String, now: Long)
    suspend fun incrementPRs(userId: String, count: Int, now: Long)
    suspend fun updateStreak(userId: String, current: Int, longest: Int, now: Long)
}

data class UserStatsRecord(
    val userId: String,
    val currentXp: Int,
    val currentLevel: Int,
    val totalXp: Int,
    val stepsToday: Int,
    val stepsGoal: Int,
    val waterGlasses: Int,
    val waterGoal: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalWorkouts: Int,
    val totalPRs: Int,
    val updatedAt: Long,
)
