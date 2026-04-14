package com.example.data.di

import com.example.data.mapper.user.UserMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val mapperModule = module {
    // Mappers (factory ou single dependendo do uso)
    singleOf(::UserMapper)
    // singleOf(::WorkoutMapper)
    // singleOf(::MealMapper)
}