package com.example.local.database.sqldelight.models

data class Configuration(
    val language: String,
    val onboardingCompleted: Boolean,
    val authToken: String
)
