package org.fitverse.project.navigation.destinations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.components.background.PremiumGamifiedBackground
import com.example.presentation.screens.ui.main.dashboard.DashboardScreen


@Composable
fun DashboardDestination(navigateToNotification: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()){
        PremiumGamifiedBackground()
        DashboardScreen(
            modifier = Modifier.fillMaxSize(),
            username = "Athlete",
            avatarInitials = "A",
            exit = {},
            onNotificationsClick = {
                navigateToNotification()
            }
        )
    }
}
