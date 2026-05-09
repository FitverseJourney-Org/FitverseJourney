package org.fitverse.project.destinations.homepage.workout

import androidx.compose.runtime.Composable
import com.example.presentation.ui.workout.WorkoutScreen

@Composable
fun WorkoutDestination(onStartWorkout: () -> Unit) {
    WorkoutScreen(
        onStartWorkout = onStartWorkout
    )
}