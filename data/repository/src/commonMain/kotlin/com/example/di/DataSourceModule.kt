package com.example.di

import com.example.local.datasource.exercises.ExerciseLocalDataSource
import com.example.local.datasource.exercises.ExerciseLocalDataSourceImpl
import com.example.local.datasource.progression.ProgressionLocalDataSource
import com.example.local.datasource.progression.ProgressionLocalDataSourceImpl
import com.example.remote.datasource.progression.ProgressionRemoteDataSource
import com.example.remote.datasource.progression.ProgressionRemoteDataSourceImpl
import com.example.remote.datasource.user.UserRemoteDataSource
import com.example.remote.datasource.user.UserRemoteDataSourceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule = module {
    // ============================================================
    // Local Data Sources
    // ============================================================
    singleOf(::ExerciseLocalDataSourceImpl) { bind<ExerciseLocalDataSource>() }
    singleOf(::ProgressionLocalDataSourceImpl) { bind<ProgressionLocalDataSource>() }
    // singleOf(::MealLocalDataSourceImpl) { bind<MealLocalDataSource>() }


    // ============================================================
    // Remote Data Sources
    // ============================================================
//    singleOf(::UserRemoteDataSourceImpl) { bind<UserRemoteDataSource>() }
    single<UserRemoteDataSource>{ UserRemoteDataSourceImpl(get()) }
    singleOf(::ProgressionRemoteDataSourceImpl) { bind<ProgressionRemoteDataSource>() }

    // No seu dataSourceModule
    single<ProgressionRemoteDataSource> { ProgressionRemoteDataSourceImpl(networkDelayMs = 600L)}
    // singleOf(::WorkoutRemoteDataSourceImpl) { bind<WorkoutRemoteDataSource>() }
    // singleOf(::MealRemoteDataSourceImpl) { bind<MealRemoteDataSource>() }
}