package org.fitverse.domain.repository.dbLocal.sqldelight.workout

import kotlinx.coroutines.flow.Flow

interface WorkoutSessionDao {
    fun observeSessionsByUser(userId: String): Flow<List<WorkoutSessionRecord>>
    suspend fun getSessionsByUser(userId: String): List<WorkoutSessionRecord>
    suspend fun getSessionsByPeriod(userId: String, from: Long, to: Long): List<WorkoutSessionRecord>
    suspend fun getRecentSessions(userId: String, limit: Long): List<WorkoutSessionRecord>
    suspend fun getSessionById(id: String): WorkoutSessionRecord?
    suspend fun insertSession(session: WorkoutSessionRecord)
    suspend fun completeSession(
        id: String, userId: String, completedAt: Long, durationSeconds: Int,
        totalVolume: Double, totalSets: Int, totalReps: Int, xpEarned: Int,
        hasPR: Boolean, intensityLevel: Int, muscleGroups: String, notes: String,
    )
    suspend fun deleteSession(id: String, userId: String)
    suspend fun countSessions(userId: String): Long
    suspend fun sumVolume(userId: String): Double
}

data class WorkoutSessionRecord(
    val id: String,
    val userId: String,
    val workoutPlanId: String,
    val workoutTitle: String,
    val startedAt: Long,
    val completedAt: Long?,
    val durationSeconds: Int,
    val totalVolume: Double,
    val totalSets: Int,
    val totalReps: Int,
    val xpEarned: Int,
    val hasPR: Boolean,
    val intensityLevel: Int,
    val muscleGroups: String,
    val notes: String,
)
