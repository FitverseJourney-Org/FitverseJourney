package com.example.domain.model.authentication.register


data class SignUp(
    val name: String,
    val email: String,
    val password: String,
    val registerGender: RegisterGender,
    val birthDate: String,
    val heightCm: Int,
    val weightKg: Double,
    val registerExperienceLevel: RegisterExperienceLevel,
    val registerGoal: RegisterGoal
)