package org.fitverse.data.local.model

data class DailyMissionEntity(
    val id: String,
    val userId: String,
    val date: String,            // YYYY-MM-DD
    val title: String,
    val description: String,
    val xpReward: Int,
    val type: String,            // MissionType name
    val isCompleted: Boolean,
    val completedAt: Long?,      // epoch ms
)
