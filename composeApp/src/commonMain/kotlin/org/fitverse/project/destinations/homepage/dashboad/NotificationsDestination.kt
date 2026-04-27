package org.fitverse.project.destinations.homepage.dashboad

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.notification.NotificationScreen


@Composable
fun NotificationDestination(toDashboard: () -> Unit) {
    NotificationScreen(
        onBack = toDashboard,
    )
}