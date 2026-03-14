package com.example.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.features.auth.repository.AuthRemoteRepositoryImpl
import com.example.data.features.auth.repository.AuthTokenStoreImpl
import com.example.data.features.cache.CacheDataSourceKeyAuthTokenImpl
import com.example.data.features.cache.CacheDataSourceKeyLanguageImpl
import com.example.data.features.cache.CacheDataSourceKeyOnboardingCompletedImpl
import com.example.domain.repository.authentication.AuthRemoteRepository
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthTokenStoreRepository
import org.koin.dsl.module

val dataModules = module {

    single { CacheDataSourceKeyOnboardingCompletedImpl(dataStore = get<DataStore<Preferences>>()) }
    single { CacheDataSourceKeyLanguageImpl(dataStore = get<DataStore<Preferences>>()) }
    single { CacheDataSourceKeyAuthTokenImpl(dataStore = get<DataStore<Preferences>>()) }

    single<AuthRepository> {
        AuthRemoteRepositoryImpl(
            remote = get<AuthRemoteRepository>(),
            tokenStore = get<AuthTokenStoreRepository>()
        )
    }
    single<AuthTokenStoreRepository> {
        AuthTokenStoreImpl()
    }

}