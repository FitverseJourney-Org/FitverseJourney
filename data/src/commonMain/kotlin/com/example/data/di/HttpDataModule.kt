package com.example.data.di

import com.example.data.dbRemote.datasources.KtorAuthRemoteDataSource
import com.example.domain.repository.authentication.AuthRemoteDataSource
import org.koin.dsl.module



val dataSourcesModules = module {
    single<AuthRemoteDataSource> {
        KtorAuthRemoteDataSource(get())
    }
}