package org.fitverse.project.destinations.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.fitverse.presentation.ui.workoutPlan.WorkoutPlanBuilderRoute

@Composable
fun WorkoutPlanBuilderDestination(
    onBack: () -> Unit,
    toAddExercises: () -> Unit,
    savePlan: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        WorkoutPlanBuilderRoute(
            onBack = onBack,
            onNavigateToAddExercise = toAddExercises,
        )
    }
}