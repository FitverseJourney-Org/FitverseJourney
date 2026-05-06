package com.example.local.expect

import com.example.local.database.datastore.storage.TokenStorage
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module

// iosMain/di/TokenStorageModule.ios.kt
val localIosModule = module {
    single<Settings> {
        @OptIn(ExperimentalSettingsImplementation::class)
        KeychainSettings(service = "com.example.app.token")
    }
    single { TokenStorage(settings = get()) }
}