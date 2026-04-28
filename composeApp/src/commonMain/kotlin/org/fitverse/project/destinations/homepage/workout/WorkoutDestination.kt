package org.fitverse.project.destinations.homepage.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.dashboard.DarkGamifiedDashboardBackground
import com.example.presentation.screens.ui.workout.WorkoutScreen

@Composable
fun WorkoutDestination() {
    Box(modifier = Modifier.fillMaxSize()){
        DarkGamifiedDashboardBackground()
        WorkoutScreen()
    }
}