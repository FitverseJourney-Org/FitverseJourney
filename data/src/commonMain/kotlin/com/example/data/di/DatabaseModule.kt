package com.example.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.database.sqldelight.DatabaseFactory
import com.example.data.database.sqldelight.DatabaseSqlDeLightHelper
import com.example.data.database.sqldelight.createDatabase
import com.example.expect.AppDataStoreDb
import com.journey.database.AppDatabase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    // DatabaseFactory (platform-specific)
    // Será fornecido por cada plataforma (Android/iOS)

    // AppDatabase (singleton)
    single<AppDatabase> { get<DatabaseFactory>().createDatabase() }

    // SQLDELIGHT
    single { DatabaseSqlDeLightHelper(driverFactory = get()) }

    singleOf(::DatabaseSqlDeLightHelper) { bind<DatabaseSqlDeLightHelper>() }

    // DATASTORE
    single<DataStore<Preferences>> { AppDataStoreDb.dataStore }
}