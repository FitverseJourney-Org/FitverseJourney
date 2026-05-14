package com.example.presentation.ui.notification.viewmodel

import com.example.presentation.ui.notification.Notification

data class NotificationUiState(
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = true,
)

sealed interface NotificationIntent {
    data object NavigateBack : NotificationIntent
    data class MarkRead(val id: String) : NotificationIntent
    data object MarkAllRead : NotificationIntent
    data class Delete(val id: String) : NotificationIntent
}

sealed interface NotificationEvent {
    data object NavigateBack : NotificationEvent
}
