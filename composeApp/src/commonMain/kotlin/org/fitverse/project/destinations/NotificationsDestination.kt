package org.fitverse.project.destinations

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.dashboard.NotificationMainScreen


@Composable
fun NotificationDestination(toDashboard: () -> Unit) {
    com.example.presentation.screens.ui.dashboard.NotificationMainScreen(
        modifier = Modifier,
        onExit = { toDashboard() }
    )
}