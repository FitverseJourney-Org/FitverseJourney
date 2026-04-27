package com.example.presentation.screens.ui.trial.state

import com.example.domain.models.activePlan.PlanId

data class TrialUiState(
    val selectedPlan : PlanId = PlanId.PRO,
    val isLoading    : Boolean = false,
    val error        : String? = null,
)