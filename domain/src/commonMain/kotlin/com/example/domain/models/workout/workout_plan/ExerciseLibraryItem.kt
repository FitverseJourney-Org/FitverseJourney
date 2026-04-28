package com.example.domain.models.workout.workout_plan

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.ui.graphics.vector.ImageVector

data class ExerciseLibraryItem(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val description: String = "Execute o movimento de forma controlada, mantendo a postura e a contração muscular alvo durante toda a amplitude.",
    val imageUrl: String = "", // Link para o GIF ou PNG
    val icon: ImageVector = Icons.Rounded.FitnessCenter
)
