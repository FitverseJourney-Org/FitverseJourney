package org.fitverse.presentation.ui.dashboard.viewmodel

import org.fitverse.presentation.ui.dashboard.DailyMission
import org.fitverse.presentation.widgets.StreakDay

data class DashboardUiState(
    val username: String = "Athlete",
    val avatarInitials: String = "A",
    val missions: List<DailyMission> = emptyList(),
    val streakDays: List<StreakDay> = emptyList(),
    val isLoading: Boolean = true,
)

sealed interface DashboardIntent {
    data class ClaimMission(val missionId: String, val missionTitle: String) : DashboardIntent
}

sealed interface DashboardEvent {
    data class ShowSnackbar(val message: String) : DashboardEvent
}
