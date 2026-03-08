package com.example.data.di

import CacheDataSourceImpl
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.features.auth.remote.AuthRemoteDataSourceImpl
import com.example.data.features.auth.repository.AuthRemoteRepositoryImpl
import com.example.data.features.auth.repository.AuthTokenStoreImpl
import com.example.domain.repository.authentication.AuthRemoteDataSource
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthTokenStoreRepository
import org.koin.dsl.module

val dataModules = module {

    single { CacheDataSourceImpl(dataStore = get<DataStore<Preferences>>()) }

    single { AuthRemoteDataSourceImpl(client = get()) }

    single<AuthRepository> {
        AuthRemoteRepositoryImpl(
            remote = get<AuthRemoteDataSource>(),
            tokenStore = get<AuthTokenStoreRepository>()
        )
    }
    single<AuthTokenStoreRepository> {
        AuthTokenStoreImpl()
    }

}