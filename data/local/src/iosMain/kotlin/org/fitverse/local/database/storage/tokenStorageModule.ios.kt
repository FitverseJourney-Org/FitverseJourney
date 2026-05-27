package org.fitverse.data.local.database.storage

import org.fitverse.data.local.datasource.token.TokenStorageImpl
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
    single { TokenStorageImpl(settings = get()) }
}