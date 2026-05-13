package com.example.presentation.ui.notification.viewmodel

import com.example.presentation.ui.notification.Notification
import com.example.presentation.ui.notification.sampleNotifications

data class NotificationUiState(
    val notifications: List<Notification> = sampleNotifications,
)

sealed interface NotificationIntent {
    data object NavigateBack : NotificationIntent
}

sealed interface NotificationEvent {
    data object NavigateBack : NotificationEvent
}
