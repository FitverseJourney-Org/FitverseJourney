package org.fitverse.project.destinations.workout

import androidx.compose.runtime.Composable
import org.fitverse.presentation.ui.workout.WorkoutScreen

@Composable
fun WorkoutDestination(
    onStartWorkout: () -> Unit,
    workoutCompletedToday: Boolean = true
) {
    WorkoutScreen(
        onStartWorkout = onStartWorkout,
        workoutCompletedToday = workoutCompletedToday
    )
}