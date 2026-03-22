package org.fitverse.project.destinations.homepage.dashboad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.notification.NotificationMainScreen


@Composable
fun NotificationDestination(toDashboard: () -> Unit) {
    NotificationMainScreen(
        modifier = Modifier,
        onExit = { toDashboard() }
    )
}