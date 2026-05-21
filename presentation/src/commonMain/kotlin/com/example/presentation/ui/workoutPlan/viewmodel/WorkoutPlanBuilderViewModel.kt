package org.fitverse.presentation.ui.workoutPlan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.presentation.ui.workoutPlan.DayOfWeek
import org.fitverse.presentation.ui.workoutPlan.WorkoutPlanEffect
import org.fitverse.presentation.ui.workoutPlan.WorkoutPlanEvent
import org.fitverse.presentation.ui.workoutPlan.WorkoutPlanUiState
import org.fitverse.presentation.ui.workoutPlan.WorkoutTemplate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class WorkoutPlanBuilderViewModel : ViewModel() {

    // ── State ──────────────────────────────────────────────────────
    private val _uiState = MutableStateFlow(WorkoutPlanUiState())
    val uiState: StateFlow<WorkoutPlanUiState> = _uiState.asStateFlow()

    // ── Effects (one-shot: navegação, erros) ──────────────────────
    private val _effects = Channel<WorkoutPlanEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    // ─────────────────────────────────────────────────────────────
    // Ponto único de entrada — toda interação da UI passa aqui
    // ─────────────────────────────────────────────────────────────

    fun onEvent(event: WorkoutPlanEvent) {
        when (event) {
            is WorkoutPlanEvent.PlanNameChanged  -> onPlanNameChanged(event.name)
            is WorkoutPlanEvent.DaySelected      -> onDaySelected(event.day)
            is WorkoutPlanEvent.RestDayToggled   -> onRestDayToggled(event.isRest)
            is WorkoutPlanEvent.TemplateSelected -> onTemplateSelected(event.template)
            is WorkoutPlanEvent.ShowTemplateSheet -> _uiState.update { it.copy(isTemplateSheetVisible = true) }
            is WorkoutPlanEvent.HideTemplateSheet -> _uiState.update { it.copy(isTemplateSheetVisible = false) }
            is WorkoutPlanEvent.SavePlan         -> onSavePlan()
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Handlers privados
    // ─────────────────────────────────────────────────────────────

    private fun onPlanNameChanged(name: String) {
        _uiState.update { it.copy(planName = name) }
    }

    private fun onDaySelected(day: DayOfWeek) {
        _uiState.update { it.copy(selectedDay = day) }
    }

    private fun onRestDayToggled(isRest: Boolean) {
        val day = _uiState.value.selectedDay
        _uiState.update { state ->
            val updatedRestDays  = state.restDays.toMutableMap().apply { set(day, isRest) }
            // Remove exercícios do dia se virou descanso
            val updatedWorkoutMap = if (isRest) {
                state.workoutMap.toMutableMap().apply { remove(day) }
            } else {
                state.workoutMap
            }
            state.copy(restDays = updatedRestDays, workoutMap = updatedWorkoutMap)
        }
    }

    private fun onTemplateSelected(template: WorkoutTemplate) {
        val templateRestDays = template.toRestDaysMap()
        _uiState.update { state ->
            // Limpa exercícios de dias que viraram descanso com o template
            val updatedWorkoutMap = state.workoutMap.toMutableMap().apply {
                templateRestDays.filter { it.value }.keys.forEach { remove(it) }
            }
            state.copy(
                restDays             = templateRestDays,
                workoutMap           = updatedWorkoutMap,
                isTemplateSheetVisible = false
            )
        }
    }

    private fun onSavePlan() {
        val state = _uiState.value
        if (state.planName.isBlank()) {
            sendEffect(WorkoutPlanEffect.ShowError("Dê um nome ao seu treino antes de salvar."))
            return
        }
        // TODO: chamar use case de persistência
        sendEffect(WorkoutPlanEffect.NavigateBack)
    }

    private fun sendEffect(effect: WorkoutPlanEffect) {
        viewModelScope.launch { _effects.send(effect) }
    }
}