package com.example.domain.models.workout.workout_plan.state

import com.example.domain.models.workout.workout_plan.DayOfWeek
import com.example.domain.models.workout.workout_plan.WorkoutCategory

data class DailyWorkoutState(
    val dayOfWeek: DayOfWeek,
    val dayNameShort: String,
    val workoutCategory: WorkoutCategory?
)
