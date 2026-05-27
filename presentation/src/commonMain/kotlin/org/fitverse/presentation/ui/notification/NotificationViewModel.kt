package org.fitverse.presentation.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.domain.usecase.notifications.DeleteNotificationUseCase
import org.fitverse.domain.usecase.notifications.MarkAllNotificationsReadUseCase
import org.fitverse.domain.usecase.notifications.MarkNotificationReadUseCase
import org.fitverse.domain.usecase.notifications.ObserveNotificationsUseCase
import org.fitverse.presentation.ui.notification.viewmodel.NotificationEvent
import org.fitverse.presentation.ui.notification.viewmodel.NotificationIntent
import org.fitverse.presentation.ui.notification.viewmodel.NotificationUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        observeNotifications()
            .filter { records ->
                records.isNotEmpty() || !_uiState.value.isLoading  // ← ignora vazio enquanto carrega
            }
            .map { records -> records.map { it.toUiModel() } }
            .onEach { notifications ->
                _uiState.update {
                    it.copy(
                        notifications = notifications,
                        unreadCount   = notifications.count { notif -> !notif.isRead },
                        isLoading     = notifications.isEmpty() && it.isLoading,
                    )
                }
            }
            .catch { _uiState.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }
}
