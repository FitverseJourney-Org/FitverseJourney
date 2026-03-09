package com.example.data.di

import com.example.data.features.cache.CacheDataSourceOnboardingImpl
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.features.auth.remote.AuthRemoteRepositoryImpl
import com.example.data.features.auth.repository.AuthRemoteRepositoryImpl
import com.example.data.features.auth.repository.AuthTokenStoreImpl
import com.example.domain.repository.authentication.AuthRemoteRepository
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthTokenStoreRepository
import org.koin.dsl.module

val dataModules = module {

    single { CacheDataSourceOnboardingImpl(dataStore = get<DataStore<Preferences>>()) }

    single { AuthRemoteRepositoryImpl(client = get()) }

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