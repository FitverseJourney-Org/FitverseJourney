package com.example.di

import com.example.domain.models.local.User
import com.example.local.mapper.EntityMapper
import com.example.local.mapper.user.UserEntityMapper
import com.example.remote.dto.user.UserDto
import com.example.remote.dto.user.UserRequestDto
import com.example.remote.mapper.DtoMapper
import com.example.remote.mapper.user.UserDtoMapper
import com.journey.database.migrations.UserEntity
import org.koin.dsl.module

val mapperModule = module {
    single<EntityMapper<UserEntity, User>> { UserEntityMapper() }
    single<DtoMapper<UserRequestDto, User>> { UserDtoMapper() }
    single { UserEntityMapper() }
    single { UserDtoMapper() }
}