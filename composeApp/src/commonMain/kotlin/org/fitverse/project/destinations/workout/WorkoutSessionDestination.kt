package org.fitverse.project.destinations.workout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.main.workout.Exercise
import com.example.presentation.screens.ui.main.workout.ExerciseType
import com.example.presentation.screens.ui.main.workout.WorkoutPlan
import com.example.presentation.screens.ui.main.workout.WorkoutSessionScreen

@Composable
fun WorkoutSessionDestination(
    navigateToWorkoutSession: () -> Unit
) {
    val exercises = remember { // Use remember para não recriar a lista em cada recomposição
        listOf(
            Exercise(id = 101, title = "Jumping Jacks", durationSeconds = 45, sets = 3, type = ExerciseType.TIMED),
            Exercise(id = 102, title = "Bodyweight Squats", reps = 15, sets = 3, type = ExerciseType.REPS),
            Exercise(id = 103, title = "Push-Ups", reps = 12, sets = 3, type = ExerciseType.REPS),
            Exercise(id = 104, title = "Plank Hold", durationSeconds = 30, sets = 2, type = ExerciseType.TIMED)
        )
    }
    var currentExercise by remember { mutableStateOf(exercises.first()) }

    WorkoutSessionScreen(
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
        workout = WorkoutPlan(
            id = 1,
            title = "Full Body Starter",
            exercises = exercises
        )
    )
}