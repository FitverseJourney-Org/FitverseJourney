package org.fitverse.project.destinations.homepage.workout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.runtime.Composable
import com.example.domain.models.workout.workout_plan.ExerciseLibraryItem
import com.example.presentation.ui.planWorkout.WorkoutPLanExerciseDetailsScreen

@Composable
fun WorkoutPlanExercisesDetailsDestination(
    onBack: () -> Unit,
    onAddExercise: (String) -> Unit,
) {
    WorkoutPLanExerciseDetailsScreen(
        exercise = ExerciseLibraryItem(
            id = "1",
            name = "Supino Reto",
            muscleGroup = "Peito",
            description = "Execute o movimento de forma controlada, mantendo a postura e a contração muscular alvo durante toda a amplitude.",
            imageUrl = "",
            icon = Icons.Rounded.FitnessCenter
        ),
        onBack = onBack,
        onAddExercise = {

        }
    )
}