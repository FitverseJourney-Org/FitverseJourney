package com.example.data.features.auth.local.model

import kotlinx.serialization.Serializable


@Serializable
data class RegisterRequest(
    val age: Int,
    val height: Int,
    val weight: Int,
    val name: String,
    val gender: String,
    val goal: String,
    val trainingLevel: String,
    val email: String,
)