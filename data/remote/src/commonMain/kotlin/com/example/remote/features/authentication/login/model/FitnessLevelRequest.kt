package com.example.remote.features.authentication.login.model

import kotlinx.serialization.Serializable

@Serializable
enum class FitnessLevelRequest {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}