package org.fitverse.project.destinations.homepage.dashboad

import androidx.compose.runtime.Composable
import com.example.presentation.ui.dashboard.DashboardScreen


@Composable
fun DashboardDestination(
    toNotification: () -> Unit,
    toEnergy: () -> Unit
) {
    DashboardScreen(
        username = "Athlete",
        avatarInitials = "A",
        exit = {},
        onNotificationsClick = {
            toNotification()
        },
        onEnergyClick = {
            toEnergy()
        }
    )
}



