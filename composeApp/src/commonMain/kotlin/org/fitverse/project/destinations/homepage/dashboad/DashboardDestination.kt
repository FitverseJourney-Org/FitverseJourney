package org.fitverse.project.destinations.homepage.dashboad

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.dashboard.DashboardScreen


@Composable
fun DashboardDestination(
    toNotification: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()){
        DashboardScreen(
            username = "Athlete",
            avatarInitials = "A",
            exit = {},
            onNotificationsClick = {
                toNotification()
            }
        )
    }
}
