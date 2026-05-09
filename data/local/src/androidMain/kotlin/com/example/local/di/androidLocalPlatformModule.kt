package com.example.local.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.local.database.datastore.DataStoreFactory
import com.example.local.database.sqldelight.DatabaseFactory
import com.example.local.datasource.token.TokenStorageImpl
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val androidLocalPlatformModule = module {
    single { DatabaseFactory(androidContext()) }
    single<DataStore<Preferences>> { DataStoreFactory.dataStore }
    single<Settings> {
        val masterKey = MasterKey.Builder(androidContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val encryptedPrefs = EncryptedSharedPreferences.create(
            androidContext(),
            "secure_token_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        SharedPreferencesSettings(delegate = encryptedPrefs)
    }
    single { TokenStorageImpl(settings = get()) }
}
