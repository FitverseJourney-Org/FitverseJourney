package com.example.presentation.screens.ui.trial.event

sealed interface TrialEvent {
    data object NavigateToDashboard : TrialEvent
    data object NavigateToLogin     : TrialEvent
    data class  ShowSnackbar(val message: String) : TrialEvent
}