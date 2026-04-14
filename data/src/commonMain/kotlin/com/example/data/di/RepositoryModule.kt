package com.example.data.di

import com.example.data.repository.user.UserRepositoryImpl
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    // UserRepository

    /*
    "quando alguém pedir um UserRepository,
    entregue essa instância de UserRepositoryImpl"
    */
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }

    // WorkoutRepository
    // singleOf(::WorkoutRepositoryImpl) { bind<WorkoutRepository>() }

    // MealRepository
    // singleOf(::MealRepositoryImpl) { bind<MealRepository>() }
}