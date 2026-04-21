package org.fitverse.project.destinations.homepage.workout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.workout.Exercise
import com.example.presentation.screens.ui.workout.ExerciseType
import com.example.presentation.screens.ui.workout.WorkoutPlan
import com.example.presentation.screens.ui.workout.WorkoutSessionScreen

@Composable
fun WorkoutSessionDestination(
    toCompletedWorkout: (resultXp: Int) -> Unit
) {
    WorkoutSessionScreen(
        modifier = Modifier,
        onFinish = {
            toCompletedWorkout(14)
        },
    )
}