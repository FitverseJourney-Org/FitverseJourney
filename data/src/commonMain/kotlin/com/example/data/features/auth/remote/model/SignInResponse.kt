package com.example.data.features.auth.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: User
)