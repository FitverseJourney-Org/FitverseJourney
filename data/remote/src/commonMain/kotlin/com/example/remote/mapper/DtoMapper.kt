package com.example.remote.mapper

import com.example.domain.models.local.User
import com.example.remote.dto.user.UserRequestDto

interface DtoMapper<Dto, Domain> {
    fun mapDtoToDomain(dto: Dto): Domain
    fun mapDomainToRequestDto(domain: Domain): Dto
}