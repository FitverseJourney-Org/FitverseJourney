package com.example.presentation.ui.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<DashboardEvent>()
    val events = _events.receiveAsFlow()

    fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.ClaimMission -> claimMission(intent.missionTitle)
        }
    }
    private fun claimMission(title: String) {
        _uiState.update { state ->
            state.copy(
                missions = state.missions.map { mission ->
                    if (mission.title == title) mission.copy(isCompleted = true) else mission
                }
            )
        }
        viewModelScope.launch {
            _events.send(DashboardEvent.ShowSnackbar("Missão concluída! XP ganho."))
        }
    }
}
