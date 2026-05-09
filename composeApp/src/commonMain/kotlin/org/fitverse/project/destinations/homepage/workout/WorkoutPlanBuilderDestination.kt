package org.fitverse.project.destinations.homepage.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.planWorkout.WorkoutPlanBuilderScreen

@Composable
fun WorkoutPlanBuilderDestination(
    onBack: () -> Unit,
    toAddExercises: () -> Unit,
    savePlan: () -> Unit
)  {
    val cs = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize()){
        WorkoutPlanBuilderScreen(
            onBack = onBack,
            toAddExercises = toAddExercises,
            savedPlan = savePlan
        )
    }
}