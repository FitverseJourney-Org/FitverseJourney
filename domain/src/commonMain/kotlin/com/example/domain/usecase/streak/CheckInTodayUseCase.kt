package org.fitverse.domain.usecase.streak

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.streak.StreakDao
import org.fitverse.domain.repository.dbLocal.sqldelight.streak.StreakRecord

class CheckInTodayUseCase(
    private val dao: StreakDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(date: String, id: String): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        val currentStreak = dao.getCurrentStreak(userId)
        dao.upsertStreak(
            StreakRecord(
                id          = id,
                userId      = userId,
                date        = date,
                isCheckedIn = true,
                streakCount = currentStreak + 1,
            )
        )
    }
}
