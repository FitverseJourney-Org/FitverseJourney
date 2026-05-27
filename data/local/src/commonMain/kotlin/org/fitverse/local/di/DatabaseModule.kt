package org.fitverse.local.di

import org.fitverse.data.local.database.sqldelight.DatabaseFactory
import org.fitverse.data.local.database.sqldelight.DatabaseSqlDeLightHelper
import com.journey.AppDatabase
import org.koin.dsl.module

val databaseModule = module {

    // DatabaseFactory é fornecido pelos módulos de plataforma (android/ios)

    // Cria AppDatabase sem depender da extension fun createDatabase()
    // — evita "uninferred T" do Koin quando o receiver é uma expect class
    single<AppDatabase> {
        val factory = get<DatabaseFactory>()
        AppDatabase(factory.createDriver())
    }

    single<DatabaseSqlDeLightHelper> {
        DatabaseSqlDeLightHelper(driverFactory = get<DatabaseFactory>())
    }
}
