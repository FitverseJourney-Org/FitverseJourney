package org.fitverse.project.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.main.dashboard.sub_screens.NotificationMainScreen


@Composable
fun NotificationDestination(navigateToDashboard: () -> Unit) {
    NotificationMainScreen(
        modifier = Modifier,
        onExit = { navigateToDashboard() }
    )
}