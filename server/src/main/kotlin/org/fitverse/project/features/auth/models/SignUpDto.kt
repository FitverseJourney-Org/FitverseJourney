package org.fitverse.project.features.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String,
    val gender: Gender,
    val birthDate: String,
    val heightCm: Int,
    val weightKg: Double,
    val fitnessLevel: FitnessLevel,
    val goal: FitnessGoal
)

@Serializable
enum class Gender { MALE, FEMALE, OTHER }

@Serializable
enum class FitnessLevel { BEGINNER, INTERMEDIATE, ADVANCED }

@Serializable
enum class FitnessGoal { LOSE_WEIGHT, GAIN_MUSCLE, MAINTAIN, IMPROVE_ENDURANCE }

// response
@Serializable
data class AuthSessionResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: PublicUser
)

@Serializable
data class PublicUser(
    val id: String,
    val name: String,
    val email: String,
    val gender: Gender? = null,
    val birthDate: String? = null,
    val heightCm: Int? = null,
    val weightKg: Double? = null,
    val fitnessLevel: FitnessLevel? = null,
    val goal: FitnessGoal? = null,
    val isPremium: Boolean = false,
    val premiumExpiresAt: Long? = null
)