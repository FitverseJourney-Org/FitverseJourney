package org.fitverse.domain.usecase.missions

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.catalog.CatalogMissionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.catalog.CatalogMissionRecord
import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord
import kotlin.random.Random

private const val DAILY_MISSION_COUNT  = 5
private const val SPECIAL_CHANCE       = 0.10

class GetDailyMissionsUseCase(
    private val catalogDao: CatalogMissionDao,
    private val missionDao: DailyMissionDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(date: String): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")

        // Seed catalog on first run
        if (catalogDao.count() == 0L) {
            catalogDao.insertAll(CatalogMissionSeeds.all)
        }

        // Already seeded for today — nothing to do
        if (missionDao.getMissionsByDate(userId, date).isNotEmpty()) return@runCatching

        val catalog  = catalogDao.getAll()
        val regular  = catalog.filter { !it.isSpecial }.shuffled()
        val special  = catalog.filter {  it.isSpecial }.shuffled()

        val hasSpecial = special.isNotEmpty() && Random.nextDouble() < SPECIAL_CHANCE
        val selected: List<CatalogMissionRecord> = if (hasSpecial) {
            regular.take(DAILY_MISSION_COUNT - 1) + special.take(1)
        } else {
            regular.take(DAILY_MISSION_COUNT)
        }

        val records = selected.mapIndexed { index, template ->
            DailyMissionRecord(
                id          = "${date}_${userId}_$index",
                userId      = userId,
                date        = date,
                title       = template.title,
                description = template.description,
                xpReward    = template.xpReward,
                type        = template.type,
                isCompleted = false,
                completedAt = null,
            )
        }

        missionDao.insertMissions(records)
    }
}
