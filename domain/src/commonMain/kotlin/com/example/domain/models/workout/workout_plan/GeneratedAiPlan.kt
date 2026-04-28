package com.example.domain.models.workout.workout_plan

data class GeneratedAiPlan(
    val title: String,
    val focus: String,
    val splitType: String, // ex: "ABCD", "Push/Pull/Legs"
    val daysPerWeek: Int,
    val level: String,
    val description: String
)