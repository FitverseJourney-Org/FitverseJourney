package com.example.remote.mapper

interface DtoMapper<Dto, Domain> {
    fun mapDtoToDomain(dto: Dto): Domain
    fun mapDomainToRequestDto(domain: Domain): Dto
}