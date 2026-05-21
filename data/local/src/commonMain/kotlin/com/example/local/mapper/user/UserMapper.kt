package org.fitverse.data.local.mapper.user


import org.fitverse.domain.models.user.User
import org.fitverse.data.local.mapper.EntityMapper
import com.journey.user.UserEntity

/**
 * Mapper para User
 * Converte entre Entity (Database) e Domain (App)
 */
// data/local/mapper/UserEntityMapper.kt
class UserEntityMapper : EntityMapper<UserEntity, User> {

    override fun mapEntityToDomain(entity: UserEntity): User {
        return User(
            uid              = entity.uid,
            name             = entity.name,
            email            = entity.email,
            genero           = entity.gender.toGeneroDomain(),           // ✅ seguro
            weight           = entity.weight,
            height           = entity.height.toInt(),
            experienceLevel  = entity.experienceLevel,
            goals            = entity.goals,
            isPremium        = entity.isPremium == 1L,
            targetWeight     = entity.targetWeight,
            targetCalories   = entity.targetCalories?.toInt(),
            targetProtein    = entity.targetProtein,
            targetCarbs      = entity.targetCarbs,
            targetFat        = entity.targetFat,
            username         = entity.username,
            birthDate        = entity.birthDate,
            classType        = entity.classType.toClassTypeDomain(),      // ✅ seguro
            lastname         = entity.lastname,
            createdAt        = entity.createdAt,
            updatedAt        = entity.updatedAt
        )
    }

    override fun mapDomainToEntity(domain: User): UserEntity {
        return UserEntity(
            uid              = domain.uid,
            name             = domain.name,
            email            = domain.email,
            lastname         = domain.lastname,
            username         = domain.username,
            birthDate        = domain.birthDate,
            gender           = domain.genero.toEntityString(),            // ✅
            classType        = domain.classType.toEntityString(),         // ✅
            weight           = domain.weight,
            height           = domain.height.toLong(),
            experienceLevel  = domain.experienceLevel,
            goals            = domain.goals,
            isPremium        = if (domain.isPremium) 1L else 0L,
            targetWeight     = domain.targetWeight,
            targetCalories   = domain.targetCalories?.toLong(),
            targetProtein    = domain.targetProtein,
            targetCarbs      = domain.targetCarbs,
            targetFat        = domain.targetFat,
            createdAt        = domain.createdAt,
            updatedAt        = domain.updatedAt
        )
    }

    fun mapEntityListToDomain(entities: List<UserEntity>): List<User> =
        entities.map { mapEntityToDomain(it) }

    fun mapDomainListToEntity(domains: List<User>): List<UserEntity> =
        domains.map { mapDomainToEntity(it) }
}