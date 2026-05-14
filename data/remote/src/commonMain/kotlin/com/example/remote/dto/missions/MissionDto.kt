package com.example.remote.dto.missions

import kotlinx.serialization.Serializable

@Serializable
data class MissionDto(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val type: String,
    val isSpecial: Boolean,
)
