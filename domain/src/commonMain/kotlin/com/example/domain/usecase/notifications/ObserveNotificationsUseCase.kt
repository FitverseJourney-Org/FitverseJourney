package com.example.domain.usecase.notifications

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.notifications.NotificationDao
import com.example.domain.repository.dbLocal.sqldelight.notifications.NotificationRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class ObserveNotificationsUseCase(
    private val dao: NotificationDao,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<List<NotificationRecord>> {
        val userId = authRepository.getCurrentUserId() ?: return emptyFlow()
        return dao.observeNotificationsByUser(userId)
    }
}
