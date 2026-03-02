package com.example.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.datasources.local.CacheDataSource
import com.example.data.auth.repository.AuthRemoteRepositoryImpl
import com.example.data.auth.repository.AuthTokenStoreImpl
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthTokenStoreRepository
import org.koin.dsl.module

val dataModules = module {

    // datasources
//    single<com.example.domain.repository.authentication.AuthRemoteDataSource> {
//        AuthRemoteDataSource(get())
//    }

    single { CacheDataSource(dataStore = get<DataStore<Preferences>>()) }



//    val dataSourcesModules = module {
//        single<com.example.domain.repository.authentication.AuthRemoteDataSource> {
//            AuthRemoteDataSource(get())
//        }
//    }

    single<AuthRepository> {
        AuthRemoteRepositoryImpl(
            remote = get<com.example.domain.repository.authentication.AuthRemoteDataSource>(),
            tokenStore = get<AuthTokenStoreRepository>()
        )
    }
    single<AuthTokenStoreRepository> {
        AuthTokenStoreImpl()
    }

}