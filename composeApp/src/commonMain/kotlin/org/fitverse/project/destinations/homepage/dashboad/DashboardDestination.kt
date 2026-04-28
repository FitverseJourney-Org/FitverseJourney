package org.fitverse.project.destinations.homepage.dashboad

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.presentation.screens.ui.dashboard.DashboardScreen


@Composable
fun DashboardDestination(
    toNotification: () -> Unit
) {
    DashboardScreen(
        username = "Athlete",
        avatarInitials = "A",
        exit = {},
        onNotificationsClick = {
            toNotification()
        }
    )
}



