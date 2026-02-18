package com.example.domain.model.authentication.register

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val gender: String,
    val age: Int,
    val height: Int,
    val goal: String,
    val weight: Int,
    val trainingLevel: String,
)
