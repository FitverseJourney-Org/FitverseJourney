package com.example.domain.models.workout.workout_plan

data class WorkoutPlanItem(
    val id: String,
    val title: String,
    val frequency: String,
    val intensity: String, // ex: "Alta", "Moderada"
    val isActive: Boolean,
    val progress: Float // 0.0f a 1.0f
)
