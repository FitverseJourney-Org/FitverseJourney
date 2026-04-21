package org.fitverse.project.di

import com.example.di.dataModule
import com.example.di.presentationModule
import com.example.domain.di.domainModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Inicialização do Koin (comum)
 */
fun initKoin(
    platformModules: List<Module>,
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    appDeclaration()
    modules(
        platformModules + listOf(
            dataModule,
            domainModule,
            presentationModule
        )
    )
}


