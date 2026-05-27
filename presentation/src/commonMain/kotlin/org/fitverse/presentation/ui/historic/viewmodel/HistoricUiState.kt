package org.fitverse.presentation.ui.historic.viewmodel

import org.fitverse.presentation.ui.historic.HistoricPeriod
import org.fitverse.presentation.ui.historic.WorkoutHistory

data class HistoricUiState(
    val history: List<WorkoutHistory> = emptyList(),
    val allHistory: List<WorkoutHistory> = emptyList(),
    val selectedPeriod: HistoricPeriod = HistoricPeriod.MONTH,
    val isPremium: Boolean = false,
    val isLoading: Boolean = true,
)

sealed interface HistoricIntent {
    data object NavigateBack : HistoricIntent
    data class SelectPeriod(val period: HistoricPeriod) : HistoricIntent
    data class DeleteSession(val sessionId: String) : HistoricIntent
}

sealed interface HistoricEvent {
    data object NavigateBack : HistoricEvent
}
