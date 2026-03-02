package com.example.domain.model

data class User(
    val id: String,
    val name: String,
    val level: Int,
    val email: String,
    val gender: String,
    val weight: Double,
    val height: Double,
    val age: Int,
    val goals: String,
    val trainingLevel: String,
    val isValidateCode: Boolean,
    val token: String
)