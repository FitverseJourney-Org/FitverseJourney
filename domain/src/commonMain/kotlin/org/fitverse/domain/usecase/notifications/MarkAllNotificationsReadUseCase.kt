package org.fitverse.domain.usecase.notifications

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.notifications.NotificationDao

class MarkAllNotificationsReadUseCase(
    private val dao: NotificationDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.markAllAsRead(userId)
    }
}
