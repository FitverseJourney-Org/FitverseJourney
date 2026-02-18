package org.fitverse.fitverseJourney.di

import com.example.data.dbLocal.repositories.LanguageRepositoryImpl
import com.example.data.dbRemote.repositories.AuthRepositoryImpl
import com.example.data.dbRemote.repositories.AuthTokenStoreImpl
import com.example.domain.repository.LanguageRepository
import com.example.domain.repository.authentication.AuthRemoteDataSource
import com.example.domain.repository.authentication.AuthRepository import com.example.domain.repository.authentication.AuthTokenStoreRepository
import org.koin.dsl.module

val repositoriesModule = module {

    single<AuthRepository> {
        AuthRepositoryImpl(
            remote = get<AuthRemoteDataSource>(),
            tokenStore = get<AuthTokenStoreRepository>()
        )
    }
    single<AuthTokenStoreRepository> {
        AuthTokenStoreImpl()
    }

    single<LanguageRepository>{
        LanguageRepositoryImpl(
            storage = get()
        )
    }

}