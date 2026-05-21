package org.fitverse.data.local.di

import org.fitverse.data.local.datasource.token.TokenStorageImpl
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings    // ← import correto iOS
import com.russhwolf.settings.Settings
import org.koin.dsl.module

@OptIn(ExperimentalSettingsImplementation::class)
val localIosModule = module {
    single<Settings> {
        KeychainSettings("secure_token_prefs")
    }
    single { TokenStorageImpl(settings = get()) }
}