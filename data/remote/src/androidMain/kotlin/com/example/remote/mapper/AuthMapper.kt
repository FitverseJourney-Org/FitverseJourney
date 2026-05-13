package com.example.remote.mapper

import com.example.domain.models.auth.AuthResult
import com.example.domain.repository.authentication.AuthResultDto

class AuthMapper {
    fun mapDtoToDomain(dto: AuthResultDto): AuthResult =
        AuthResult(
            uid   = dto.uid,
            email = dto.email ?: "",
            token = dto.token ?: "",
        )
}