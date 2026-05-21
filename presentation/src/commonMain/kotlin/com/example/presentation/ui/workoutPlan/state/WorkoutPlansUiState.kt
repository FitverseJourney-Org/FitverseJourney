package org.fitverse.presentation.ui.workoutPlan.state

import org.fitverse.domain.models.workoutPlan.WorkoutPlan

const val MAX_PLANS_ALLOWED = 2

data class WorkoutPlansUiState(
    val plans: List<WorkoutPlan> = emptyList(),

    /** Plan currently selected by user tap (triggers action menu) */
    val selectedPlan: WorkoutPlan? = null,

    /** Controls Create Plan bottom sheet visibility */
    val showCreateBottomSheet: Boolean = false,

    /** Controls Delete confirmation dialog visibility */
    val showDeleteDialog: Boolean = false,

    /** Plan pending deletion, used to populate the dialog */
    val planToDelete: WorkoutPlan? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    /** The currently active plan shown featured at top */
    val activePlan: WorkoutPlan? get() = plans.firstOrNull { it.isActive }

    /** Non-active plans shown in the "Meus Planos" section */
    val inactivePlans: List<WorkoutPlan> get() = plans.filter { !it.isActive }

    /** True when the plan limit has been reached */
    val isAtLimit: Boolean get() = plans.size >= MAX_PLANS_ALLOWED

    val planCount: String get() = "${plans.size}/$MAX_PLANS_ALLOWED"
}
