package com.example.domain.models.workout.workout_plan

data class WorkoutCategory(
    val id: String = "1",
    val name: String,
    val description: List<ExerciseModel>,
    val exercises: List<ExerciseModel> = emptyList()
)
