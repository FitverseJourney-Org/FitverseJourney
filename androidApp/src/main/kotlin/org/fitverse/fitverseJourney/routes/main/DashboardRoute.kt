package org.fitverse.fitverseJourney.routes.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.main.dashboard.DashboardScreen

@Composable
fun DashboardRoute(
    modifier: Modifier,
    onExit: () -> Unit,
    navigateToNotification: () -> Unit
) {
    DashboardScreen(
        modifier = modifier,
        username = "Athlete",
        avatarInitials = "A",
        exit = onExit,
        onNotificationsClick = navigateToNotification,
    )
}