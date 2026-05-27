package org.fitverse.data.remote.mapper.user

import org.fitverse.domain.models.user.Genero
import org.fitverse.domain.models.user.User
import org.fitverse.domain.utils.toBirthDateString
import org.fitverse.domain.utils.toEpochMillis
import org.fitverse.remote.dto.user.UserRequestDto
import org.fitverse.data.remote.mapper.DtoMapper

class UserDtoMapper : DtoMapper<UserRequestDto, User> {

    // DTO vem do servidor com birthDate em "DD/MM/YYYY"; converte para epoch millis
    override fun mapDtoToDomain(dto: UserRequestDto): User = User(
        uid       = dto.uid,
        name      = dto.name,
        lastname  = dto.lastname,
        username  = dto.username,
        email     = dto.email,
        genero    = Genero.entries.firstOrNull { it.name == dto.gender } ?: Genero.MASCULINO,
        birthDate = dto.birthDate.toEpochMillis(),
        weight    = dto.weight,
        height    = dto.height,
        isPremium = dto.isPremium,
        photoUrl  = dto.photoUrl,
        createdAt = dto.createdAt,
        updatedAt = dto.updatedAt,
    )

    // Serializa de volta para o servidor: epoch millis → "DD/MM/YYYY"
    override fun mapDomainToRequestDto(domain: User): UserRequestDto = UserRequestDto(
        uid       = domain.uid,
        name      = domain.name,
        lastname  = domain.lastname,
        username  = domain.username,
        email     = domain.email,
        gender    = domain.genero.name,
        birthDate = domain.birthDate.toBirthDateString(),
        weight    = domain.weight,
        height    = domain.height,
        isPremium = domain.isPremium,
        photoUrl  = domain.photoUrl,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt,
    )
}
