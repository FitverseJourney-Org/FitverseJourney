package org.fitverse.project.features.auth.models

import kotlinx.serialization.Serializable


@Serializable
data class UserToken(
    val tokenId: String? = null,
    val refreshToken: String? = null, // Útil para manter a sessão ativa
    val expiresIn: Long? = null      // Timestamp de quando o token expira
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String? = null
)

@Serializable
data class GenericResponse(
    val success: Boolean,
    val message: String
)
