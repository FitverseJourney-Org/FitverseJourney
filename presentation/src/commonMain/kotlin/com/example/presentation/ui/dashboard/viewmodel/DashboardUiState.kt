package com.example.presentation.ui.dashboard.viewmodel

import com.example.presentation.ui.dashboard.DailyMission
import com.example.presentation.ui.dashboard.DailyMission.Companion.defaultDailyMissions
import com.example.presentation.widgets.StreakDay

data class DashboardUiState(
    val username: String = "Athlete",
    val avatarInitials: String = "A",
    val missions: List<DailyMission> = defaultDailyMissions,
    val streakDays: List<StreakDay> = listOf(
        StreakDay("S", isCompleted = true),
        StreakDay("T", isCompleted = true),
        StreakDay("Q", isCompleted = true),
        StreakDay("Q", isCompleted = true),
        StreakDay("S", isCompleted = true),
        StreakDay("S", isCompleted = true),
        StreakDay("D", isCompleted = true),
    ),
)

sealed interface DashboardIntent {
    data class ClaimMission(val missionTitle: String) : DashboardIntent
}

sealed interface DashboardEvent {
    data class ShowSnackbar(val message: String) : DashboardEvent
}
