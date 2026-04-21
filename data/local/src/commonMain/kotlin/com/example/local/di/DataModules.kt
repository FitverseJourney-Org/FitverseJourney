package com.example.local.di

import com.example.local.database.datastore.repository.AppAuthenticateRepositoryImpl
import com.example.local.database.datastore.repository.AppLanguageRepositoryImpl
import com.example.local.database.datastore.repository.AppOnboardingRepositoryImpl
import com.example.local.database.sqldelight.repository.configuration.ConfigLanguageDataSourceDao
import com.example.local.database.sqldelight.repository.configuration.ConfigOnboardingDataSourceDao
import com.example.local.database.sqldelight.repository.configuration.ConfigTokenDaoDataSourceImpl
import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.domain.repository.dbLocal.datastore.AppOnboardingRepository
import com.example.domain.repository.dbLocal.sqldelight.configurations.ConfigTokenDataSourceDao
import org.koin.dsl.module

val dataModules = module {



    // IMPLEMENTS DO DATASTORE
    single<AppOnboardingRepository> { AppOnboardingRepositoryImpl(dataStore = get()) }
    single<AppAuthenticateRepository> { AppAuthenticateRepositoryImpl(dataStore = get()) }
    single<AppLanguageRepository> { AppLanguageRepositoryImpl(dataStore = get()) }

    // REPOSITORIES DO DATASTORE
//    single<UserRepository>{UserDaoRepositoryImpl(databaseSqlDeLightHelper = get()) }

    // DATASTORE sqldelight
    single<ConfigLanguageDataSourceDao> { ConfigLanguageDataSourceDao(databaseSqlDeLightHelper = get()) }
    single<ConfigOnboardingDataSourceDao> { ConfigOnboardingDataSourceDao(databaseSqlDeLightHelper = get()) }
    single<ConfigTokenDataSourceDao> { ConfigTokenDaoDataSourceImpl(databaseSqlDeLightHelper = get()) }
}