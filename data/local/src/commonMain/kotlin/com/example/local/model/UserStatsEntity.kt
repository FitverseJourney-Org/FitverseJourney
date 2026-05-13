package com.example.local.model

data class UserStatsEntity(
    val userId: String,
    val currentXp: Int,
    val currentLevel: Int,
    val totalXp: Int,
    val stepsToday: Int,
    val stepsGoal: Int,
    val waterGlasses: Int,
    val waterGoal: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalWorkouts: Int,
    val totalPRs: Int,
    val updatedAt: Long,    // epoch ms
)
