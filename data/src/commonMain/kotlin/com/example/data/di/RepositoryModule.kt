package com.example.data.di

import com.example.data.database.datastore.repository.AppAuthenticateRepositoryImpl
import com.example.data.database.datastore.repository.AppLanguageRepositoryImpl
import com.example.data.database.datastore.repository.AppOnboardingRepositoryImpl
import com.example.data.database.sqldelight.repository.configuration.ConfigLanguageDataSourceDao
import com.example.data.database.sqldelight.repository.configuration.ConfigOnboardingDataSourceDao
import com.example.data.database.sqldelight.repository.configuration.ConfigTokenDaoDataSourceImpl
import com.example.data.features.auth.repository.AuthRemoteRepositoryImpl
import com.example.data.features.auth.repository.AuthTokenStoreImpl
import com.example.data.repository.user.UserRepositoryImpl
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthTokenStoreRepository
import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.domain.repository.dbLocal.datastore.AppOnboardingRepository
import com.example.domain.repository.dbLocal.sqldelight.configurations.ConfigTokenDataSourceDao
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
    singleOf(::AppOnboardingRepositoryImpl) { bind<AppOnboardingRepository>() }
    singleOf(::AppAuthenticateRepositoryImpl) { bind<AppAuthenticateRepository>() }
    singleOf(::AppLanguageRepositoryImpl) { bind<AppLanguageRepository>() }

    // REPOSITORIES DO DATASTORE
    singleOf(::AuthRemoteRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::AuthTokenStoreImpl) { bind<AuthTokenStoreRepository>() }
    // singleOf(::UserDaoRepositoryImpl) { bind<UserRepository>() }

    // DATASTORE sqldelight
    singleOf(::ConfigLanguageDataSourceDao)
    singleOf(::ConfigOnboardingDataSourceDao)
    singleOf(::ConfigTokenDaoDataSourceImpl) { bind<ConfigTokenDataSourceDao>() }

    // WorkoutRepository
    // singleOf(::WorkoutRepositoryImpl) { bind<WorkoutRepository>() }

    // MealRepository
    // singleOf(::MealRepositoryImpl) { bind<MealRepository>() }
}