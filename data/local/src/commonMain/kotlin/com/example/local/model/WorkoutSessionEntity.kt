package com.example.local.model

data class WorkoutSessionEntity(
    val id: String,
    val userId: String,
    val workoutPlanId: String,
    val workoutTitle: String,
    val startedAt: Long,         // epoch ms
    val completedAt: Long?,      // epoch ms, null = em andamento
    val durationSeconds: Int,
    val totalVolume: Double,     // kg total
    val totalSets: Int,
    val totalReps: Int,
    val xpEarned: Int,
    val hasPR: Boolean,
    val intensityLevel: Int,     // 1-5
    val muscleGroups: String,    // separado por vírgula
    val notes: String,
)
