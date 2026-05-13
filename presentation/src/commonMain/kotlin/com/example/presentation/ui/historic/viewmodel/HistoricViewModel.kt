package com.example.presentation.ui.historic.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HistoricViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HistoricUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<HistoricEvent>()
    val events = _events.receiveAsFlow()

    fun onIntent(intent: HistoricIntent) {
        when (intent) {
            HistoricIntent.NavigateBack -> viewModelScope.launch {
                _events.send(HistoricEvent.NavigateBack)
            }
        }
    }
}
