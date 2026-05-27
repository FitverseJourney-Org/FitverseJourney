package org.fitverse.domain.usecase.notifications

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.notifications.NotificationDao

class DeleteNotificationUseCase(
    private val dao: NotificationDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(notificationId: String): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.deleteNotification(notificationId, userId)
    }
}
