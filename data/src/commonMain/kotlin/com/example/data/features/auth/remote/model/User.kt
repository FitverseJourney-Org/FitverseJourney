package com.example.data.features.auth.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val gender: Gender,
    val birthDate: String,
    val heightCm: Int,
    val weightKg: Double,
    val fitnessLevel: FitnessLevel,
    val goal: FitnessGoal,
    val isPremium: Boolean
)