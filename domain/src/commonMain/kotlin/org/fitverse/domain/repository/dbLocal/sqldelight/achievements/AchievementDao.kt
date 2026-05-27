package org.fitverse.domain.repository.dbLocal.sqldelight.achievements

import kotlinx.coroutines.flow.Flow

interface AchievementDao {
    fun observeAchievementsByUser(userId: String): Flow<List<AchievementRecord>>
    suspend fun getAchievementsByUser(userId: String): List<AchievementRecord>
    suspend fun getAchievementsByCategory(userId: String, category: String): List<AchievementRecord>
    suspend fun getAchievementById(id: String, userId: String): AchievementRecord?
    suspend fun getUnlockedAchievements(userId: String): List<AchievementRecord>
    suspend fun insertAchievement(achievement: AchievementRecord)
    suspend fun insertAchievements(achievements: List<AchievementRecord>)
    suspend fun updateProgress(id: String, userId: String, progress: Double, status: String)
    suspend fun unlockAchievement(id: String, userId: String, unlockedAt: String)
    suspend fun countUnlocked(userId: String): Long
}

data class AchievementRecord(
    val id: String,
    val userId: String,
    val icon: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val rarity: String,
    val status: String,
    val category: String,
    val progress: Double,
    val maxProgress: Double,
    val condition: String,
    val unlockedAt: String?,
)
