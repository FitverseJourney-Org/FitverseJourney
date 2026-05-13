package com.example.local.model

data class WorkoutSetEntity(
    val id: String,
    val sessionId: String,
    val exerciseName: String,
    val muscleGroup: String,
    val setNumber: Int,
    val reps: Int,
    val weight: Double,    // kg
    val isPR: Boolean,
    val rpe: Int?,         // 1-10, nullable
    val notes: String,
)
