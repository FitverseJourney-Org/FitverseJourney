package com.example.remote.features.authentication.login.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: User
)
