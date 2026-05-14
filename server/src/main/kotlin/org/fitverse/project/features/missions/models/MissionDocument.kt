package org.fitverse.project.features.missions.models

import kotlinx.serialization.Serializable

// Firestore document — default values required for Firebase deserialization
data class MissionDocument(
    val title: String = "",
    val description: String = "",
    val xpReward: Int = 10,
    val type: String = "STRETCH",
    val isSpecial: Boolean = false,
)

@Serializable
data class MissionResponse(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val type: String,
    val isSpecial: Boolean,
)
