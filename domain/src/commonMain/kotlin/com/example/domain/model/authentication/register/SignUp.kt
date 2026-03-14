package com.example.domain.model.authentication.register


data class SignUp(
    val name: String,
    val email: String,
    val password: String,
    val gender: Gender,
    val birthDate: String,
    val heightCm: Int,
    val weightKg: Double,
    val fitnessLevel: FitnessLevel,
    val fitnessGoal: FitnessGoal
)