package com.example.presentation.screens.ui.trial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.activePlan.PlanId
import com.example.domain.usecase.activatePlan.ActivatePlanUseCase
import com.example.presentation.screens.ui.trial.event.TrialEvent
import com.example.presentation.screens.ui.trial.state.TrialUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrialViewModel(
    private val activatePlan: ActivatePlanUseCase,
) : ViewModel() {

    // ── State ─────────────────────────────────────────────────────────────────

    private val _state = MutableStateFlow(TrialUiState())
    val state: StateFlow<TrialUiState> = _state.asStateFlow()

    // ── Events (one-shot — Canal evita reentrega ao remontar) ─────────────────

    private val _events = Channel<TrialEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // ── Intent handlers ───────────────────────────────────────────────────────

    fun onPlanSelected(planId: PlanId) {
        _state.update { it.copy(selectedPlan = planId) }
    }

    fun onActivate() {
        val planId = _state.value.selectedPlan

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            activatePlan(planId)
                .onSuccess {
                    _events.send(TrialEvent.NavigateToDashboard)
                }
                .onFailure { throwable ->
                    val msg = throwable.message ?: "Erro ao ativar plano"
                    _state.update { it.copy(error = msg) }
                    _events.send(TrialEvent.ShowSnackbar(msg))
                }

            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onDismiss() {
        viewModelScope.launch { _events.send(TrialEvent.NavigateToLogin) }
    }
}