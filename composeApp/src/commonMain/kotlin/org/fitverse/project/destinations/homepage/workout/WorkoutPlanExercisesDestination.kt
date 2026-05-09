package org.fitverse.project.destinations.homepage.workout


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.planWorkout.WorkoutPlanExercisesScreen

@Composable
fun WorkoutPlanExercisesDestination(
    onBack: () -> Unit,
    onAddExercise: (String) -> Unit,
    onDetails: () -> Unit
)  {
    val cs = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize()){
        WorkoutPlanExercisesScreen(
            onBack = onBack,
            onAddExercise = {},
            onDetails =onDetails
        )
    }
}