package org.fitverse.presentation.ui.notification.viewmodel

import org.fitverse.presentation.ui.notification.NotificationUiModel

data class NotificationUiState(
    val notifications: List<NotificationUiModel> = emptyList(),
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
