package org.fitverse.project.destinations.workout

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
    navigateToWorkoutSession: () -> Unit
) {
    val exercises = remember { // Use remember para não recriar a lista em cada recomposição
        listOf(
            com.example.presentation.screens.ui.workout.Exercise(
                id = 101,
                title = "Jumping Jacks",
                durationSeconds = 45,
                sets = 3,
                type = com.example.presentation.screens.ui.workout.ExerciseType.TIMED
            ),
            com.example.presentation.screens.ui.workout.Exercise(
                id = 102,
                title = "Bodyweight Squats",
                reps = 15,
                sets = 3,
                type = com.example.presentation.screens.ui.workout.ExerciseType.REPS
            ),
            com.example.presentation.screens.ui.workout.Exercise(
                id = 103,
                title = "Push-Ups",
                reps = 12,
                sets = 3,
                type = com.example.presentation.screens.ui.workout.ExerciseType.REPS
            ),
            com.example.presentation.screens.ui.workout.Exercise(
                id = 104,
                title = "Plank Hold",
                durationSeconds = 30,
                sets = 2,
                type = com.example.presentation.screens.ui.workout.ExerciseType.TIMED
            )
        )
    }
    var currentExercise by remember { mutableStateOf(exercises.first()) }

    com.example.presentation.screens.ui.workout.WorkoutSessionScreen(
        modifier = Modifier,
        onFinish = {
            navigateToWorkoutSession()
        },
        changeExercise = {
            currentExercise = it
        },
        currentExercise = {
            currentExercise
        },
        workout = com.example.presentation.screens.ui.workout.WorkoutPlan(
            id = 1,
            title = "Full Body Starter",
            exercises = exercises
        )
    )
}