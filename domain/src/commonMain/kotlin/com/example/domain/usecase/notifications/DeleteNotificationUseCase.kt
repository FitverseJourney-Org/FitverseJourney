package com.example.domain.usecase.notifications

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.notifications.NotificationDao

class DeleteNotificationUseCase(
    private val dao: NotificationDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(notificationId: String): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.deleteNotification(notificationId, userId)
    }
}
