package org.fitverse.project.destinations.workout

import androidx.compose.runtime.Composable
import com.example.presentation.ui.workout.WorkoutCompletionResult
import com.example.presentation.ui.workout.WorkoutCompletionScreen

@Composable
fun WorkoutCompletedDestination(
    result: WorkoutCompletionResult,
    onContinue: () -> Unit
) {
    WorkoutCompletionScreen(
        result = result,
        onContinue = onContinue,
    )
}
