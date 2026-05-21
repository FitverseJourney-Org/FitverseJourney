package org.fitverse.project.destinations.workout

import androidx.compose.runtime.Composable
import org.fitverse.presentation.ui.workout.WorkoutCompletionResult
import org.fitverse.presentation.ui.workout.WorkoutCompletionScreen

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
