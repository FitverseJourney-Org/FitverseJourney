package com.example.local.database

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.local.database.datastore.DataStoreFactory
import com.example.local.database.sqldelight.DatabaseFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val androidLocalPlatformModule = module {
    single { DatabaseFactory(androidContext()) }
    single<DataStore<Preferences>> { DataStoreFactory.dataStore }
}
