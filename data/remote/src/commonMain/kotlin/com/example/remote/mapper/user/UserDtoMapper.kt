package org.fitverse.data.remote.mapper.user

import org.fitverse.domain.models.user.ClassType
import org.fitverse.domain.models.user.Genero
import org.fitverse.domain.models.user.User
import org.fitverse.data.remote.dto.user.UserRequestDto
import org.fitverse.data.remote.mapper.DtoMapper

class UserDtoMapper : DtoMapper<UserRequestDto, User> {

    override fun mapDtoToDomain(dto: UserRequestDto): User = User(
        uid             = dto.uid,
        name            = dto.name,
        email           = dto.email,
        lastname        = dto.lastname,
        username        = dto.username,
        birthDate       = dto.birthDate,
        genero          = Genero.valueOf(dto.gender),
        classType       = ClassType.valueOf(dto.classType),
        weight          = dto.weight,
        height          = dto.height,
        experienceLevel = dto.experienceLevel,
        goals           = dto.goals,
        isPremium       = dto.isPremium,
        targetWeight    = dto.targetWeight,
        targetCalories  = dto.targetCalories,
        targetProtein   = dto.targetProtein,
        targetCarbs     = dto.targetCarbs,
        targetFat       = dto.targetFat,
        createdAt       = dto.createdAt,
        updatedAt       = dto.updatedAt

    )

    // ✅ novo — converte Domain → RequestDto para chamadas de escrita
    override fun mapDomainToRequestDto(domain: User): UserRequestDto = UserRequestDto(
        uid = domain.uid,
        name = domain.name,
        email = domain.email,
        lastname = domain.lastname,
        username = domain.username,
        birthDate = domain.birthDate,
        gender = domain.genero.name,
        classType = domain.classType.name,
        weight = domain.weight,
        height = domain.height,
        experienceLevel = domain.experienceLevel,
        goals = domain.goals,
        isPremium = domain.isPremium,
        targetWeight = domain.targetWeight,
        targetCalories = domain.targetCalories,
        targetProtein = domain.targetProtein,
        targetCarbs = domain.targetCarbs,
        targetFat = domain.targetFat,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}