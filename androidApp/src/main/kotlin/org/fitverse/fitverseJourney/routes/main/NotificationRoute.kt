package org.fitverse.fitverseJourney.routes.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.main.dashboard.NotificationMainScreen

@Composable
fun NotificationRoute(
    modifier: Modifier,
    onExit: () -> Unit,
) {
    NotificationMainScreen(
        modifier = modifier,
        onExit = onExit
    )
}