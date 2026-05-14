package com.example.domain.usecase.notifications

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.notifications.NotificationDao

class MarkAllNotificationsReadUseCase(
    private val dao: NotificationDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        dao.markAllAsRead(userId)
    }
}
