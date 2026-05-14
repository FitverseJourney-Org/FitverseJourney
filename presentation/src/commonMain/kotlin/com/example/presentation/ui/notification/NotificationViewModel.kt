package com.example.presentation.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.notification.NotificationType
import com.example.domain.repository.dbLocal.sqldelight.notifications.NotificationRecord
import com.example.domain.usecase.notifications.DeleteNotificationUseCase
import com.example.domain.usecase.notifications.MarkAllNotificationsReadUseCase
import com.example.domain.usecase.notifications.MarkNotificationReadUseCase
import com.example.domain.usecase.notifications.ObserveNotificationsUseCase
import com.example.presentation.ui.notification.viewmodel.NotificationEvent
import com.example.presentation.ui.notification.viewmodel.NotificationIntent
import com.example.presentation.ui.notification.viewmodel.NotificationUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import com.example.expect.PlatformDateFormatter
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class NotificationViewModel(
    private val observeNotifications: ObserveNotificationsUseCase,
    private val markRead: MarkNotificationReadUseCase,
    private val markAllRead: MarkAllNotificationsReadUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<NotificationEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeNotificationStream()
    }

    fun onIntent(intent: NotificationIntent) {
        when (intent) {
            NotificationIntent.NavigateBack -> viewModelScope.launch {
                _events.send(NotificationEvent.NavigateBack)
            }
            is NotificationIntent.MarkRead -> viewModelScope.launch {
                markRead(intent.id)
            }
            NotificationIntent.MarkAllRead -> viewModelScope.launch {
                markAllRead()
            }
            is NotificationIntent.Delete -> viewModelScope.launch {
                deleteNotification(intent.id)
            }
        }
    }

    private fun observeNotificationStream() {
        viewModelScope.launch {
            observeNotifications()
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { records ->
                    _uiState.update {
                        it.copy(
                            notifications = records.map { r -> r.toUi() },
                            unreadCount   = records.count { r -> !r.isRead },
                            isLoading     = false,
                        )
                    }
                }
        }
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun NotificationRecord.toUi() = Notification(
        type        = runCatching { NotificationType.valueOf(type) }.getOrDefault(NotificationType.SISTEMA),
        title       = title,
        description = description,
        time        = createdAt.toRelativeTime(),
        isUnread    = !isRead,
    )

    private fun Long.toRelativeTime(): String {
        val nowMs = PlatformDateFormatter.getCurrentTimeMillis()
        val diffMs = nowMs - this
        val minutes = diffMs / 60_000
        val hours   = minutes / 60
        val days    = hours / 24
        return when {
            minutes < 60  -> "${minutes}min atrás"
            hours   < 24  -> "${hours}h atrás"
            days    < 7   -> "${days}d atrás"
            else          -> {
                val dt = Instant.fromEpochMilliseconds(this)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                "${dt.dayOfMonth}/${dt.monthNumber}/${dt.year}"
            }
        }
    }
}
