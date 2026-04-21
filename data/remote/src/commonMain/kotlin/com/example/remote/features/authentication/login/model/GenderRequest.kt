package com.example.remote.features.authentication.login.model

import kotlinx.serialization.Serializable

@Serializable
enum class GenderRequest {
    MALE,
    FEMALE,
    OTHER
}