package com.example.presentation.screens.ui.dashboard.state

data class AvatarState(
    val name: String = "Your Avatar",
    var level: Int = 23,
    var xp: Int = 340,
    val xpToNext: Int = 500,
    val health: Int = 0,
    val points: Int = 0,
    val todayPts: Int,
    var food: Int = 60,
    var consecutiveDays: Int = 0,
    var hp: Int = 0,
)
