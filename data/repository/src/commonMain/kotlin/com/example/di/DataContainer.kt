package com.example.di


import com.example.local.di.dataModules
import com.example.local.di.databaseModule
import com.example.remote.util.networkModule
import org.koin.dsl.module

/**
 * Módulo principal do Data Layer
 * Combina todos os submódulos
 */
val dataModule = module {
    includes(
        databaseModule,
        dataModules,
        networkModule,
        dataSourceModule,
        repositoryModule,
        mapperModule
    )
}