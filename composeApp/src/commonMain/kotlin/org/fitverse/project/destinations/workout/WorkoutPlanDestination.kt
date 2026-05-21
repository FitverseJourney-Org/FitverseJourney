package org.fitverse.project.destinations.workout

import androidx.compose.runtime.Composable
import org.fitverse.presentation.ui.workoutPlan.WorkoutPlansRoot

@Composable
fun WorkoutPlanDestination(
    onBack: () -> Unit,
    toNewWorkout: () -> Unit
) {
    WorkoutPlansRoot(
        onBack = onBack,
        onNewPlan = toNewWorkout
    )
}