package org.fitverse.project.destinations.workout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.fitverse.presentation.ui.workout.WorkoutCompletionResult
import org.fitverse.presentation.ui.workout.WorkoutSessionScreen

@Composable
fun WorkoutSessionDestination(
    toCompletedWorkout: (WorkoutCompletionResult) -> Unit
) {
    WorkoutSessionScreen(
        modifier = Modifier,
        onFinish = toCompletedWorkout,
    )
}
