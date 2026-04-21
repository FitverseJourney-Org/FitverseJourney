package com.example.di

import com.example.local.database.datastore.repository.AppAuthenticateRepositoryImpl
import com.example.local.database.datastore.repository.AppLanguageRepositoryImpl
import com.example.local.database.datastore.repository.AppOnboardingRepositoryImpl
import com.example.local.database.sqldelight.repository.configuration.ConfigLanguageDataSourceDao
import com.example.local.database.sqldelight.repository.configuration.ConfigOnboardingDataSourceDao
import com.example.local.database.sqldelight.repository.configuration.ConfigTokenDaoDataSourceImpl
import com.example.remote.datasource.friends.FriendsRemoteDataSourceImpl
import com.example.domain.repository.FakeFriendsRepository
import com.example.domain.repository.FriendsRepository
import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.domain.repository.dbLocal.datastore.AppOnboardingRepository
import com.example.domain.repository.dbLocal.sqldelight.configurations.ConfigTokenDataSourceDao
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    // UserRepository

    /*
    "quando alguém pedir um UserRepository,
    entregue essa instância de UserRepositoryImpl"
    */
    singleOf(::AppOnboardingRepositoryImpl) { bind<AppOnboardingRepository>() }
    singleOf(::AppAuthenticateRepositoryImpl) { bind<AppAuthenticateRepository>() }
    singleOf(::AppLanguageRepositoryImpl) { bind<AppLanguageRepository>() }
    // singleOf(::UserDaoRepositoryImpl) { bind<UserRepository>() }

    // DATASTORE sqldelight
    singleOf(::ConfigLanguageDataSourceDao)
    singleOf(::ConfigOnboardingDataSourceDao)
    singleOf(::ConfigTokenDaoDataSourceImpl) { bind<ConfigTokenDataSourceDao>() }

    singleOf(::FriendsRemoteDataSourceImpl) { bind<FriendsRepository>() }
    singleOf(::FakeFriendsRepository)
    // WorkoutRepository
    // singleOf(::WorkoutRepositoryImpl) { bind<WorkoutRepository>() }

    // MealRepository
    // singleOf(::MealRepositoryImpl) { bind<MealRepository>() }
}