package com.example.presentation.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presentation.ui.notification.viewmodel.NotificationEvent
import com.example.presentation.ui.notification.viewmodel.NotificationIntent
import com.example.presentation.ui.notification.viewmodel.NotificationUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<NotificationEvent>()
    val events = _events.receiveAsFlow()

    fun onIntent(intent: NotificationIntent) {
        when (intent) {
            NotificationIntent.NavigateBack -> viewModelScope.launch {
                _events.send(NotificationEvent.NavigateBack)
            }
        }
    }
}
