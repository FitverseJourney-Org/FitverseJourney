package org.fitverse.project.destinations.homepage.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.planWorkout.WorkoutPlanScreen

@Composable
fun WorkoutPlanDestination(
    onBack: () -> Unit,
    toNewWorkout: () -> Unit
)  {
    val cs = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize()){
        WorkoutPlanScreen(
            onBack = onBack,
            onEditWorkout = {},
            toNewWorkout = toNewWorkout
        )
    }
}