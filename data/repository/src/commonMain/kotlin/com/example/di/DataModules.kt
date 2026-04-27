package com.example.di

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
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository
import com.example.local.datasource.user.UserLocalDataSource
import com.example.local.datasource.user.UserLocalDataSourceImpl
import com.example.remote.datasource.activePlan.ActivatePlanRemoteDataSource
import com.example.remote.datasource.activePlan.ActivatePlanRemoteDataSourceImpl
import com.example.remote.datasource.user.UserRemoteDataSource
import com.example.remote.datasource.user.UserRemoteDataSourceImpl
import com.example.repository.UserRepositoryImpl
import org.koin.dsl.module

val dataModules = module {



    // IMPLEMENTS DO DATASTORE
    single<AppOnboardingRepository> { AppOnboardingRepositoryImpl(dataStore = get()) }
    single<AppAuthenticateRepository> { AppAuthenticateRepositoryImpl(dataStore = get()) }
    single<AppLanguageRepository> { AppLanguageRepositoryImpl(dataStore = get()) }
    single<UserRepository> {
        UserRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get(),
            entityMapper = get(),
            dtoMapper = get(),
            networkMonitor = get()
        )
    }
    single<UserLocalDataSource> {
        UserLocalDataSourceImpl(
            database = get(),
        )
    }
    single<UserRemoteDataSource>{
        UserRemoteDataSourceImpl(
            httpClient = get()
        )
    }

    single<ActivatePlanRemoteDataSource> {
        ActivatePlanRemoteDataSourceImpl(
            api = get()
        )
    }

    // REPOSITORIES DO DATASTORE
    //    single<UserRepository>{UserDaoRepositoryImpl(databaseSqlDeLightHelper = get()) }

    // DATASTORE sqldelight
    single<ConfigLanguageDataSourceDao> { ConfigLanguageDataSourceDao(databaseSqlDeLightHelper = get()) }
    single<ConfigOnboardingDataSourceDao> { ConfigOnboardingDataSourceDao(databaseSqlDeLightHelper = get()) }
    single<ConfigTokenDataSourceDao> { ConfigTokenDaoDataSourceImpl(databaseSqlDeLightHelper = get()) }
}