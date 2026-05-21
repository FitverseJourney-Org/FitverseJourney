package org.fitverse.data.remote.mapper

import org.fitverse.domain.models.auth.AuthResult
import org.fitverse.data.remote.dto.auth.AuthResultDto

class AuthMapper {
    fun mapDtoToDomain(dto: AuthResultDto): AuthResult = AuthResult(
        uid   = dto.uid,
        email = dto.email ?: "",
        token = dto.token ?: "",
    )

}