package com.example.data.features.auth.local.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val token: String
)

