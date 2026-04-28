package com.example.domain.models.workout.workout_plan

data class WorkoutScreenState(
    val objective: String = "Hipertrofia Muscular",
    val experienceLevel: String = "Intermediário",
    val workoutsPerWeek: Int = 5,
    val completedThisWeek: Int = 3,
    val activePlan: WorkoutPlanItem? = null,
    val availablePlans: List<WorkoutPlanItem> = emptyList()
)
