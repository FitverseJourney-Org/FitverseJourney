package com.example.data.features.auth.model.signUp

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String,
    val genderRequest: GenderRequest,
    val birthDate: String,
    val heightCm: Int,
    val weightKg: Double,
    val fitnessLevelRequest: FitnessLevelRequest,
    val fitnessGoal: FitnessGoalRequest
)