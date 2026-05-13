package com.example.presentation.ui.historic.viewmodel

import com.example.presentation.ui.historic.WorkoutHistory
import com.example.presentation.ui.historic.mockHistory

data class HistoricUiState(
    val history: List<WorkoutHistory> = mockHistory,
    val isPremium: Boolean = false,
)

sealed interface HistoricIntent {
    data object NavigateBack : HistoricIntent
}

sealed interface HistoricEvent {
    data object NavigateBack : HistoricEvent
}
