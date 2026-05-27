package org.fitverse.presentation.ui.workoutPlan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.domain.models.workoutPlan.WorkoutPlan
import org.fitverse.domain.usecase.workoutPlan.WorkoutPlanUseCases
import org.fitverse.presentation.ui.workoutPlan.state.WorkoutPlansUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutPlansViewModel(
    private val useCases: WorkoutPlanUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutPlansUiState(isLoading = true))
    val uiState: StateFlow<WorkoutPlansUiState> = _uiState.asStateFlow()

    init {
        loadPlans()
    }

    private fun loadPlans() {
        viewModelScope.launch {
            useCases.getWorkoutPlans().collect { plans ->
                _uiState.update { it.copy(plans = plans, isLoading = false) }
            }
        }
    }

    // ──────────────────── Create Flow ────────────────────

    fun onShowCreateSheet() {
        _uiState.update { it.copy(showCreateBottomSheet = true) }
    }

    fun onDismissCreateSheet() {
        _uiState.update { it.copy(showCreateBottomSheet = false) }
    }

    /** Simulates creating a plan via AI */
    fun onCreateWithAI() {
        viewModelScope.launch {
//            useCases.addPlan(WorkoutPlanRepositoryImpl.mockAIPlan)
            _uiState.update { it.copy(showCreateBottomSheet = false) }
        }
    }

    /** Simulates creating a plan manually */
    fun onCreateManually() {
        viewModelScope.launch {
//            useCases.addPlan(WorkoutPlanRepositoryImpl.mockManualPlan)
            _uiState.update { it.copy(showCreateBottomSheet = false) }
        }
    }

    // ──────────────────── Selection / Actions ────────────────────

    fun onSelectPlan(plan: WorkoutPlan) {
        _uiState.update { state ->
            val alreadySelected = state.selectedPlan?.id == plan.id
            state.copy(selectedPlan = if (alreadySelected) null else plan)
        }
    }

    fun onDismissSelection() {
        _uiState.update { it.copy(selectedPlan = null) }
    }

    fun onActivatePlan(planId: String) {
        viewModelScope.launch {
            useCases.activatePlan(planId)
            _uiState.update { it.copy(selectedPlan = null) }
        }
    }

    fun onEditPlan(plan: WorkoutPlan) {
        // Navigation to edit screen handled externally via callback
        _uiState.update { it.copy(selectedPlan = null) }
    }

    // ──────────────────── Delete Flow ────────────────────

    fun onRequestDeletePlan(plan: WorkoutPlan) {
        _uiState.update {
            it.copy(
                showDeleteDialog = true,
                planToDelete = plan,
                selectedPlan = null
            )
        }
    }

    fun onConfirmDelete() {
        val planId = _uiState.value.planToDelete?.id ?: return
        viewModelScope.launch {
            useCases.deleteWorkoutPlan(planId)
            _uiState.update { it.copy(showDeleteDialog = false, planToDelete = null) }
        }
    }

    fun onCancelDelete() {
        _uiState.update { it.copy(showDeleteDialog = false, planToDelete = null) }
    }

    // ──────────────────── Error ────────────────────

    fun onClearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
