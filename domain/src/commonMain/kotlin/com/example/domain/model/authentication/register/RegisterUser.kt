package com.example.domain.model.authentication.register


data class RegisterUser(
    val name: String,
    val email: String,
    val password: String,
    val registerGender: RegisterGender,
    val age: Int,
    val height: Int,
    val weight: Double,
    val experienceLevel: RegisterExperienceLevel,
    val goal: RegisterGoal
)