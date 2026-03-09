package org.fitverse.project.features.auth.db

import org.fitverse.project.features.auth.models.FitnessGoal
import org.fitverse.project.features.auth.models.FitnessLevel
import org.fitverse.project.features.auth.models.Gender

data class UserEntity(
    val id: String,
    val name: String,
    val email: String,
    val passwordHash: String,
    val gender: Gender?,
    val birthDate: String?,
    val heightCm: Int?,
    val weightKg: Double?,
    val fitnessLevel: FitnessLevel?,
    val goal: FitnessGoal?,
    val isPremium: Boolean = false,
    val premiumExpiresAt: Long? = null
)