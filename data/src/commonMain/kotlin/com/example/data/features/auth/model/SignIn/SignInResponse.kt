package com.example.data.features.auth.model.SignIn

import com.example.data.features.auth.model.User
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: User
)