package org.fitverse.data.local.mapper.user

import com.journey.user.UserEntity
import org.fitverse.domain.models.user.User
import org.fitverse.data.local.mapper.EntityMapper

class UserEntityMapper : EntityMapper<UserEntity, User> {

    override fun mapEntityToDomain(entity: UserEntity): User = User(
        uid       = entity.uid,
        name      = entity.name,
        lastname  = entity.lastname,
        username  = entity.username,
        email     = entity.email,
        genero    = entity.gender.toGeneroDomain(),
        birthDate = entity.birthDate,           // Long → Long
        weight    = entity.weight,
        height    = entity.height.toInt(),
        isPremium = entity.isPremium == 1L,
        photoUrl  = entity.photoUrl,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt,
    )

    override fun mapDomainToEntity(domain: User): UserEntity = UserEntity(
        uid       = domain.uid,
        name      = domain.name,
        lastname  = domain.lastname,
        username  = domain.username,
        email     = domain.email,
        gender    = domain.genero.toEntityString(),
        birthDate = domain.birthDate,           // Long → Long
        weight    = domain.weight,
        height    = domain.height.toLong(),
        isPremium = if (domain.isPremium) 1L else 0L,
        photoUrl  = domain.photoUrl,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt,
    )

    fun mapEntityListToDomain(entities: List<UserEntity>): List<User> =
        entities.map { mapEntityToDomain(it) }

    fun mapDomainListToEntity(domains: List<User>): List<UserEntity> =
        domains.map { mapDomainToEntity(it) }
}
