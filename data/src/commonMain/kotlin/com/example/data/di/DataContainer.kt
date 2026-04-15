package com.example.data.di

import org.koin.dsl.module

/**
 * Módulo principal do Data Layer
 * Combina todos os submódulos
 */
val dataModule = module {
    includes(
        databaseModule,
        networkModule,
        repositoryModule,
        dataSourceModule,
        mapperModule
    )
}