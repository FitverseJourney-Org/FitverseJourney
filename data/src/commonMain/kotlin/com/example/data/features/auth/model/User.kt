package com.example.data.features.auth.model

import com.example.data.features.auth.model.signUp.FitnessGoalRequest
import com.example.data.features.auth.model.signUp.FitnessLevelRequest
import com.example.data.features.auth.model.signUp.GenderRequest
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val genderRequest: GenderRequest,
    val birthDate: String,
    val heightCm: Int,
    val weightKg: Double,
    val fitnessLevelRequest: FitnessLevelRequest,
    val fitnessGoal: FitnessGoalRequest,
    val isPremium: Boolean
)