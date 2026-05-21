package org.fitverse.data.remote.dto.auth

data class AuthResultDto(
    val uid: String,
    val email: String?,
    val token: String?
)
