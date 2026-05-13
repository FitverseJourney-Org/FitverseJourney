package org.fitverse.project.destinations.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.activity.ActivityScreen
import com.example.presentation.widgets.DarkGamifiedDashboardBackground

@Composable
fun ActivityDestination(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        DarkGamifiedDashboardBackground()
        ActivityScreen(modifier = modifier)
    }
}
