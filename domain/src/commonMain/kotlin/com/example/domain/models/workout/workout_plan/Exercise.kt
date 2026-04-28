package com.example.domain.models.workout.workout_plan

data class Exercise(
    val id: String = "1",
    val name: String,
    val sets: String = "3",
    val reps: String = "12",
    val rest: String = "60s"
)