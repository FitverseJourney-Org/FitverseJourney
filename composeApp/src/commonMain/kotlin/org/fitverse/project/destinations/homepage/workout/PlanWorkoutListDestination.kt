package org.fitverse.project.destinations.homepage.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.presentation.screens.ui.planWorkout.WorkoutPlanDashboardScreen

@Composable
fun PlanWorkoutListDestination(
    onBack: () -> Unit,
    toNewWorkout: () -> Unit
)  {
    val cs = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize()){
        WorkoutPlanDashboardScreen(
            onBack = onBack,
            onEditWorkout = {},
            toNewWorkout = toNewWorkout
        )
    }
}