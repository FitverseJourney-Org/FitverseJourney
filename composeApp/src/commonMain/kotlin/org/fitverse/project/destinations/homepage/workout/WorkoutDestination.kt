package org.fitverse.project.destinations.homepage.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.workout.WorkoutScreenV2
import org.fitverse.project.destinations.homepage.dashboad.DarkGamifiedDashboardBackground

@Composable
fun WorkoutDestination(
    toWorkoutSession: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()){
        DarkGamifiedDashboardBackground()
        WorkoutScreenV2(
            onStart = {
                toWorkoutSession()
            }
        )

    }
}