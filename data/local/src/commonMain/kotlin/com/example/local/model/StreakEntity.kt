package com.example.local.model

data class StreakEntity(
    val id: String,
    val userId: String,
    val date: String,        // YYYY-MM-DD
    val isCheckedIn: Boolean,
    val streakCount: Int,
)
