package com.example.domain.repository.dbLocal.sqldelight.missions

import kotlinx.coroutines.flow.Flow

interface DailyMissionDao {
    fun observeMissionsByDate(userId: String, date: String): Flow<List<DailyMissionRecord>>
    suspend fun getMissionsByDate(userId: String, date: String): List<DailyMissionRecord>
    suspend fun insertMission(mission: DailyMissionRecord)
    suspend fun insertMissions(missions: List<DailyMissionRecord>)
    suspend fun completeMission(id: String, userId: String, completedAt: Long)
    suspend fun resetMissionsForDate(userId: String, date: String)
    suspend fun deleteMission(id: String, userId: String)
    suspend fun deleteMissionsOlderThan(userId: String, beforeDate: String)
    suspend fun countCompleted(userId: String, date: String): Long
}

data class DailyMissionRecord(
    val id: String,
    val userId: String,
    val date: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val type: String,
    val isCompleted: Boolean,
    val completedAt: Long?,
)
