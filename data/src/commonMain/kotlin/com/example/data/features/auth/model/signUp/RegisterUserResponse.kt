package com.example.data.features.auth.model.signUp

import com.example.data.features.auth.model.User
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: User
)
