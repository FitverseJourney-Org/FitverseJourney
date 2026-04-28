package com.example.domain.models.workout.workout_plan

data class ExerciseModel(
    val id: String = "1",
    val name: String,
    val sets: Int,
    val reps: String,
    val restTimeSeconds: Int = 60,
    val weightKgs: Double? = null
)
