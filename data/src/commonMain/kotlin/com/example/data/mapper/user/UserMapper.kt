// ============================================================
// MAPPER
// ============================================================
// Arquivo: data/src/commonMain/kotlin/com/example/data/mapper/migrations/UserMapper.kt

package com.example.data.mapper.user


import com.example.data.mapper.base.Mapper
import com.example.data.model.dto.user.UserDto
import com.example.domain.model.local.User
import com.journey.database.migrations.UserEntity

/**
 * Mapper para User
 * Converte entre Entity (Database) e Domain (App)
 */
class UserMapper : Mapper<UserEntity, User, UserDto> {

    // ✅ SQLDelight (UserEntity) → Domínio (User)
    override fun mapEntityToDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            gender = entity.gender,
            age = entity.age.toInt(),
            weight = entity.weight,
            height = entity.height.toInt(),
            experienceLevel = entity.experienceLevel,
            goals = entity.goals,
            isPremium = entity.isPremium == 1L,
            targetWeight = entity.targetWeight,
            targetCalories = entity.targetCalories.toInt(),
            targetProtein = entity.targetProtein,
            targetCarbs = entity.targetCarbs,
            targetFat = entity.targetFat,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    // ✅ Domínio (User) → SQLDelight (UserEntity)
    override fun mapDomainToEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id ?: 0L,
            name = domain.name,
            email = domain.email,
            gender = domain.gender,
            age = domain.age.toLong(),
            weight = domain.weight,
            height = domain.height.toLong(),
            experienceLevel = domain.experienceLevel,
            goals = domain.goals,
            isPremium = if (domain.isPremium) 1L else 0L,
            targetWeight = domain.targetWeight,
            targetCalories = domain.targetCalories.toLong(),
            targetProtein = domain.targetProtein,
            targetCarbs = domain.targetCarbs,
            targetFat = domain.targetFat,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    // ✅ API DTO (UserDto) → Domínio (User)
    override fun mapDtoToDomain(dto: UserDto): User {
        return User(
            id = dto.id,
            name = dto.name,
            email = dto.email,
            gender = dto.gender,
            age = dto.age,
            weight = dto.weight,
            height = dto.height.toInt(),
            experienceLevel = dto.experienceLevel,
            goals = dto.goals,
            isPremium = dto.isPremium,
            targetWeight = dto.targetWeight ?: 0.0,
            targetCalories = dto.targetCalories ?: 0,
            targetProtein = dto.targetProtein ?: 0.0,
            targetCarbs = dto.targetCarbs ?: 0.0,
            targetFat = dto.targetFat ?: 0.0,
            createdAt = dto.createdAt ?: 0L,
            updatedAt = dto.updatedAt ?: 0L
        )
    }

    // ✅ Domínio (User) → API DTO (UserDto)
    override fun mapDomainToDto(domain: User): UserDto {
        return UserDto(
            id = domain.id ?: 0L,
            name = domain.name,
            email = domain.email,
            gender = domain.gender,
            age = domain.age,
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

    // ✅ Listas — tipos corretos agora
    fun mapEntityListToDomainList(entities: List<UserEntity>): List<User> =
        entities.map { mapEntityToDomain(it) }

    fun mapDtoListToDomainList(dtos: List<UserDto>): List<User> =
        dtos.map { mapDtoToDomain(it) }
}