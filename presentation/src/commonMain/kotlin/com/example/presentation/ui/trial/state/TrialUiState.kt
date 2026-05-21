package org.fitverse.presentation.ui.trial.state

import org.fitverse.domain.models.PlanId

data class TrialUiState(
    val selectedPlan : PlanId = PlanId.PRO,
    val isLoading    : Boolean = false,
    val error        : String? = null,
)