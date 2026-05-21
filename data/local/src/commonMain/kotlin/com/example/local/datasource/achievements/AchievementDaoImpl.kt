package org.fitverse.data.local.datasource.achievements

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import org.fitverse.domain.repository.dbLocal.sqldelight.achievements.AchievementDao
import org.fitverse.domain.repository.dbLocal.sqldelight.achievements.AchievementRecord
import com.journey.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AchievementDaoImpl(database: AppDatabase) : AchievementDao {

    private val queries = database.achievementEntityQueries

    override fun observeAchievementsByUser(userId: String): Flow<List<AchievementRecord>> =
        queries.selectAllAchievementsByUser(userId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toRecord() } }

    override suspend fun getAchievementsByUser(userId: String): List<AchievementRecord> =
        withContext(Dispatchers.IO) {
            queries.selectAllAchievementsByUser(userId).executeAsList().map { it.toRecord() }
        }

    override suspend fun getAchievementsByCategory(userId: String, category: String): List<AchievementRecord> =
        withContext(Dispatchers.IO) {
            queries.selectAchievementsByCategory(userId, category).executeAsList().map { it.toRecord() }
        }

    override suspend fun getAchievementById(id: String, userId: String): AchievementRecord? =
        withContext(Dispatchers.IO) {
            queries.selectAchievementById(id, userId).executeAsOneOrNull()?.toRecord()
        }

    override suspend fun getUnlockedAchievements(userId: String): List<AchievementRecord> =
        withContext(Dispatchers.IO) {
            queries.selectUnlockedAchievements(userId).executeAsList().map { it.toRecord() }
        }

    override suspend fun insertAchievement(achievement: AchievementRecord): Unit =
        withContext(Dispatchers.IO) {
            queries.insertAchievement(
                id          = achievement.id,
                userId      = achievement.userId,
                icon        = achievement.icon,
                title       = achievement.title,
                description = achievement.description,
                xpReward    = achievement.xpReward.toLong(),
                rarity      = achievement.rarity,
                status      = achievement.status,
                category    = achievement.category,
                progress    = achievement.progress,
                maxProgress = achievement.maxProgress,
                condition   = achievement.condition,
                unlockedAt  = achievement.unlockedAt,
            )
        }

    override suspend fun insertAchievements(achievements: List<AchievementRecord>): Unit =
        withContext(Dispatchers.IO) { achievements.forEach { insertAchievement(it) } }

    override suspend fun updateProgress(id: String, userId: String, progress: Double, status: String): Unit =
        withContext(Dispatchers.IO) {
            queries.updateAchievementProgress(progress = progress, status = status, id = id, userId = userId)
        }

    override suspend fun unlockAchievement(id: String, userId: String, unlockedAt: String): Unit =
        withContext(Dispatchers.IO) {
            queries.unlockAchievement(unlockedAt = unlockedAt, id = id, userId = userId)
        }

    override suspend fun countUnlocked(userId: String): Long =
        withContext(Dispatchers.IO) { queries.countUnlockedByUser(userId).executeAsOne() }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun com.journey.achievement.AchievementEntity.toRecord() = AchievementRecord(
        id          = id,
        userId      = userId,
        icon        = icon,
        title       = title,
        description = description,
        xpReward    = xpReward.toInt(),
        rarity      = rarity,
        status      = status,
        category    = category,
        progress    = progress,
        maxProgress = maxProgress,
        condition   = condition,
        unlockedAt  = unlockedAt,
    )
}
