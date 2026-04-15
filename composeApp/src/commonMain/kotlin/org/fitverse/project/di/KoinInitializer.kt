package org.fitverse.project.di

import com.example.data.di.dataModule
import com.example.di.presentationModule
import com.example.domain.di.domainModule
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
        dataModule,
        domainModule,
        presentationModule
    )
}