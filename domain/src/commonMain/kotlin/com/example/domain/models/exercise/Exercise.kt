package org.fitverse.domain.models.exercise

data class Exercise(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val restSeconds: Int
)
