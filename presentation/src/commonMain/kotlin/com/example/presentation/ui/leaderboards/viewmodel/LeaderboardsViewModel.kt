package com.example.presentation.ui.leaderboards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaderboardsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardsUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<LeaderboardsEvent>()
    val events = _events.receiveAsFlow()

    fun onIntent(intent: LeaderboardsIntent) {
        when (intent) {
            LeaderboardsIntent.NavigateBack -> viewModelScope.launch {
                _events.send(LeaderboardsEvent.NavigateBack)
            }
            is LeaderboardsIntent.FilterScope  -> _uiState.update { it.copy(scope  = intent.scope)  }
            is LeaderboardsIntent.FilterMetric -> _uiState.update { it.copy(metric = intent.metric) }
            is LeaderboardsIntent.FilterPeriod -> _uiState.update { it.copy(period = intent.period) }
        }
    }
}
