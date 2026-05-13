package com.example.presentation.ui.achievements.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AchievementsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<AchievementsEvent>()
    val events = _events.receiveAsFlow()

    fun onIntent(intent: AchievementsIntent) {
        when (intent) {
            AchievementsIntent.NavigateBack -> viewModelScope.launch {
                _events.send(AchievementsEvent.NavigateBack)
            }
        }
    }
}
