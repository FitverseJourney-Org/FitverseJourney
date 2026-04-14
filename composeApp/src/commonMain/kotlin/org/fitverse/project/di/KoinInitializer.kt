package org.fitverse.project.di

import com.example.data.di.dataModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

/**
 * Inicialização do Koin (comum)
 */
fun initKoin(
    platformModule: Module,
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    appDeclaration()
    modules(
        platformModule,  // Módulo específico da plataforma
        dataModule,      // Módulo de dados
        // domainModule, // Módulo de domínio (use cases)
        // presentationModule // Módulo de apresentação (ViewModels)
    )
}