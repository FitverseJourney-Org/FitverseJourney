package org.fitverse.project.destinations.dashboad

import androidx.compose.runtime.Composable
import org.fitverse.presentation.ui.dashboard.DashboardRoot
import org.fitverse.presentation.ui.dashboard.viewmodel.DashboardViewModel

@Composable
fun DashboardDestination(
    viewModel:           DashboardViewModel,
    toNotification:      () -> Unit,
    toEnergy:            () -> Unit,
    onNavigateToWorkout: () -> Unit = {},
) {
    DashboardRoot(
        viewModel            = viewModel,
        exit                 = {},
        onNotificationsClick = toNotification,
        onEnergyClick        = toEnergy,
        onNavigateToWorkout  = onNavigateToWorkout,
    )
}
