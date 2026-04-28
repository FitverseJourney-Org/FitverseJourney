package com.example.presentation.screens.ui.planWorkout.components

import com.example.domain.models.workout.workout_plan.ExerciseModel

// --- MOCKS ---
fun getMockExercises(variation: Int): List<ExerciseModel> {
    val names = listOf("Supino", "Agachamento", "Remada", "Rosca Direta", "Leg Press", "Desenvolvimento")
    return List(4) { i ->
        ExerciseModel(id = "$variation$i", name = names.random(), sets = 4, reps = "10-12")
    }
}