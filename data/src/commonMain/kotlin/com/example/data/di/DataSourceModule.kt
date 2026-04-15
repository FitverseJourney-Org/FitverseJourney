package com.example.data.di

import com.example.data.datasource.local.cache.PreferencesDataSource
import com.example.data.datasource.local.cache.PreferencesDataSourceImpl
import com.example.data.datasource.local.user.UserLocalDataSource
import com.example.data.datasource.local.user.UserLocalDataSourceImpl
import com.example.data.datasource.remote.user.UserRemoteDataSource
import com.example.data.datasource.remote.user.UserRemoteDataSourceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule = module {
    // ============================================================
    // Local Data Sources
    // ============================================================
    singleOf(::UserLocalDataSourceImpl) { bind<UserLocalDataSource>() }
    singleOf(::PreferencesDataSourceImpl) { bind<PreferencesDataSource>() }
    // singleOf(::MealLocalDataSourceImpl) { bind<MealLocalDataSource>() }


    // ============================================================
    // Remote Data Sources
    // ============================================================
    singleOf(::UserRemoteDataSourceImpl) { bind<UserRemoteDataSource>() }
    // singleOf(::WorkoutRemoteDataSourceImpl) { bind<WorkoutRemoteDataSource>() }
    // singleOf(::MealRemoteDataSourceImpl) { bind<MealRemoteDataSource>() }
}