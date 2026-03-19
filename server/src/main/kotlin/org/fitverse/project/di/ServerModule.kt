package org.fitverse.project.di

import org.fitverse.project.features.auth.controller.AuthController
import org.fitverse.project.features.auth.services.AuthService
import org.koin.dsl.module

val authModule = module {
    // Define o Service primeiro (quem faz a lógica)
    single { AuthService(get()) } // o get() busca o FirebaseAdmin ou DB se necessário

    // Define o Controller (quem recebe as chamadas das rotas)
    single { AuthController(get()) }
}

val appModule = listOf(authModule)