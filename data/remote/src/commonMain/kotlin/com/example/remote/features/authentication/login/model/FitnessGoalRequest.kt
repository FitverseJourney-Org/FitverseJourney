package com.example.remote.features.authentication.login.model

import kotlinx.serialization.Serializable

@Serializable
enum class FitnessGoalRequest {
    LOSE_WEIGHT,
    GAIN_MUSCLE,
    MAINTAIN,
    IMPROVE_ENDURANCE
}