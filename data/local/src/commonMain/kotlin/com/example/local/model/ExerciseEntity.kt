package com.example.local.model

data class ExerciseEntity(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val trainingSplit: String,
    val isActive: Boolean = true,
)
