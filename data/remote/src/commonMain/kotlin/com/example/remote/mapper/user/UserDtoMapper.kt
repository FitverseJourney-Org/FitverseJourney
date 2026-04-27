package com.example.remote.mapper.user

import com.example.domain.models.local.ClassType
import com.example.domain.models.local.Genero
import com.example.domain.models.local.User
import com.example.remote.dto.user.UserDto
import com.example.remote.dto.user.UserRequestDto
import com.example.remote.mapper.DtoMapper

class UserDtoMapper : DtoMapper<UserRequestDto, User> {

    override fun mapDtoToDomain(dto: UserRequestDto): User = User(
        uid             = dto.uid,
        name            = dto.name,
        email           = dto.email,
        lastname        = dto.lastname,
        username        = dto.username,
        birthDate       = dto.birthDate,
        gender          = Genero.valueOf(dto.gender),
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
        createdAt       = dto.createdAt.toLong(),
        updatedAt       = dto.updatedAt.toLong()

    )

    // ✅ novo — converte Domain → RequestDto para chamadas de escrita
    override fun mapDomainToRequestDto(domain: User): UserRequestDto = UserRequestDto(
        uid = domain.uid,
        name = domain.name,
        email = domain.email,
        lastname = domain.lastname,
        username = domain.username,
        birthDate = domain.birthDate,
        gender = domain.gender.name,
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
        createdAt = "",
        updatedAt = ""

    )

}