package com.example.data.features.auth.remote.model

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
enum class Gender {
    MALE,
    FEMALE,
    OTHER
}

@Serializable
enum class FitnessLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

@Serializable
enum class FitnessGoal {
    LOSE_WEIGHT,
    GAIN_MUSCLE,
    MAINTAIN,
    IMPROVE_ENDURANCE
}