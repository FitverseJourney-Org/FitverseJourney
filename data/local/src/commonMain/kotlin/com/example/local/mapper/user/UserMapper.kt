package com.example.local.mapper.user


import com.example.domain.models.local.ClassType
import com.example.domain.models.local.Genero
import com.example.domain.models.local.User
import com.example.local.mapper.EntityMapper
import com.journey.database.migrations.UserEntity

/**
 * Mapper para User
 * Converte entre Entity (Database) e Domain (App)
 */
// data/local/mapper/UserEntityMapper.kt
class UserEntityMapper : EntityMapper<UserEntity, User> {

    override fun mapEntityToDomain(entity: UserEntity): User {
        return User(
            uid = entity.uid,
            name = entity.name,
            email = entity.email,
            gender = Genero.valueOf(entity.gender),
            weight = entity.weight,
            height = entity.height.toInt(),
            experienceLevel = entity.experienceLevel,
            goals = entity.goals,
            isPremium = entity.isPremium == 1L,
            targetWeight = entity.targetWeight,
            targetCalories = entity.targetCalories?.toInt(),
            targetProtein = entity.targetProtein,
            targetCarbs = entity.targetCarbs,
            targetFat = entity.targetFat,
            username = entity.username,
            birthDate = entity.birthDate,
            classType = ClassType.valueOf(entity.classType),
            lastname = entity.lastname,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun mapDomainToEntity(domain: User): UserEntity {
        return UserEntity(
            uid = domain.uid,
            name = domain.name,
            email = domain.email,
            lastname = domain.lastname,
            username = domain.username,
            birthDate = domain.birthDate,
            gender = domain.gender.name,
            classType = domain.classType.name,
            weight = domain.weight,
            height = domain.height.toLong(),
            experienceLevel = domain.experienceLevel,
            goals = domain.goals,
            isPremium = if (domain.isPremium) 1L else 0L,
            targetWeight = domain.targetWeight,
            targetCalories = domain.targetCalories?.toLong(),
            targetProtein = domain.targetProtein,
            targetCarbs = domain.targetCarbs,
            targetFat = domain.targetFat,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    fun mapEntityListToDomain(entities: List<UserEntity>): List<User> =
        entities.map { mapEntityToDomain(it) }

    fun mapDomainListToEntity(domains: List<User>): List<UserEntity> =
        domains.map { mapDomainToEntity(it) }
}