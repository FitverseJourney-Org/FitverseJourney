package org.fitverse.data.local.di

import org.fitverse.data.local.database.sqldelight.DatabaseFactory
import org.fitverse.data.local.database.sqldelight.DatabaseSqlDeLightHelper
import org.fitverse.data.local.database.sqldelight.createDatabase
import com.journey.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    // DatabaseFactory fornecido por cada plataforma (Android/iOS)

    // AppDatabase (singleton)
    single<AppDatabase> { get<DatabaseFactory>().createDatabase() }

    // SQLDelight
    single { DatabaseSqlDeLightHelper(driverFactory = get()) }

    // DataStore<Preferences> fornecido por androidLocalPlatformModule / iosLocalPlatformModule
}