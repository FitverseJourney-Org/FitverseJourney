package org.fitverse.project.destinations.workout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.workout.WorkoutCompletionResult
import com.example.presentation.ui.workout.WorkoutSessionScreen

@Composable
fun WorkoutSessionDestination(
    toCompletedWorkout: (WorkoutCompletionResult) -> Unit
) {
    WorkoutSessionScreen(
        modifier = Modifier,
        onFinish = toCompletedWorkout,
    )
}
