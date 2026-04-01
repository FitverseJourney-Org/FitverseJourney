package com.example.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.database.DatabaseSqlDeLightHelper
import com.example.data.features.auth.repository.AuthRemoteRepositoryImpl
import com.example.data.features.auth.repository.AuthTokenStoreImpl
import com.example.data.features.dbLocal.datastore.repository.AppAuthenticateRepositoryImpl
import com.example.data.features.dbLocal.datastore.repository.AppLanguageRepositoryImpl
import com.example.data.features.dbLocal.datastore.repository.AppOnboardingRepositoryImpl
import com.example.data.features.dbLocal.sqldelight.repository.configuration.ConfigLanguageDataSourceDao
import com.example.data.features.dbLocal.sqldelight.repository.configuration.ConfigOnboardingDataSourceDao
import com.example.data.features.dbLocal.sqldelight.repository.configuration.ConfigTokenDaoDataSourceImpl
import com.example.data.features.dbLocal.sqldelight.repository.user.UserDaoRepositoryImpl
import com.example.domain.repository.authentication.AuthRemoteRepository
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthTokenStoreRepository
import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.domain.repository.dbLocal.datastore.AppOnboardingRepository
import com.example.domain.repository.dbLocal.datastore.AppPreferencesRepository
import com.example.domain.repository.dbLocal.sqldelight.configurations.ConfigTokenDataSourceDao
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepositoryDao
import com.example.expect.AppDataStoreDb
import org.koin.dsl.module

val dataModules = module {

    // DATASTORE
    single<DataStore<Preferences>> { AppDataStoreDb.dataStore }
    single<AppOnboardingRepository> { AppOnboardingRepositoryImpl(dataStore = get()) }
    single<AppAuthenticateRepository> { AppAuthenticateRepositoryImpl(dataStore = get()) }
    single<AppLanguageRepository> { AppLanguageRepositoryImpl(dataStore = get()) }

    single<AuthRepository> {AuthRemoteRepositoryImpl(remote = get<AuthRemoteRepository>(),tokenStore = get<AuthTokenStoreRepository>())}
    single<AuthTokenStoreRepository> { AuthTokenStoreImpl() }

    single<UserRepositoryDao>{UserDaoRepositoryImpl(databaseSqlDeLightHelper = get()) }


    single { DatabaseSqlDeLightHelper(driverFactory = get()) }

    // DATASTORE sqldelight
    single<ConfigLanguageDataSourceDao> { ConfigLanguageDataSourceDao(databaseSqlDeLightHelper = get()) }
    single<ConfigOnboardingDataSourceDao> { ConfigOnboardingDataSourceDao(databaseSqlDeLightHelper = get()) }
    single<ConfigTokenDataSourceDao> { ConfigTokenDaoDataSourceImpl(databaseSqlDeLightHelper = get()) }

}