package com.example.data.features.auth.model.signUp

import kotlinx.serialization.Serializable

@Serializable
enum class FitnessGoalRequest {
    LOSE_WEIGHT,
    GAIN_MUSCLE,
    MAINTAIN,
    IMPROVE_ENDURANCE
}