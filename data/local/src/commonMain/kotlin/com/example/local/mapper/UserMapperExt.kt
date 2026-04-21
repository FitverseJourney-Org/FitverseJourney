package com.example.local.mapper

import com.example.data.model.dto.user.UserDto
import com.example.domain.model.local.User
import com.journey.database.migrations.UserEntity

// ============================================================
// DTO (API) → Domínio
// ============================================================
fun UserDto.toDomain(mapper: UserMapper): User =
    mapper.mapDtoToDomain(this)

// Corrigido: era 'fromEntity...' mas o receptor é List<UserDto>
fun List<UserDto>.toDomainListFromDto(mapper: UserMapper): List<User> =
    this.map { mapper.mapDtoToDomain(it) }

// ============================================================
// Entity (SQLDelight) → Domínio
// ============================================================
fun UserEntity.toDomain(mapper: UserMapper): User =
    mapper.mapEntityToDomain(this)

fun List<UserEntity>.toDomainListFromEntity(mapper: UserMapper): List<User> =
    this.map { mapper.mapEntityToDomain(it) }

// ============================================================
// Domínio → Entity (SQLDelight)
// ============================================================
fun User.toEntity(mapper: UserMapper): UserEntity =
    mapper.mapDomainToEntity(this)

fun User?.toEntityOrNull(mapper: UserMapper): UserEntity? =
    this?.let { mapper.mapDomainToEntity(it) }

fun List<User>.toEntityList(mapper: UserMapper): List<UserEntity> =
    this.map { mapper.mapDomainToEntity(it) }

// ============================================================
// Domínio → DTO (API)
// ============================================================
fun User.toDto(mapper: UserMapper): UserDto =
    mapper.mapDomainToDto(this)

fun User?.toDtoOrNull(mapper: UserMapper): UserDto? =
    this?.let { mapper.mapDomainToDto(it) }

fun List<User>.toDtoList(mapper: UserMapper): List<UserDto> =
    this.map { mapper.mapDomainToDto(it) }