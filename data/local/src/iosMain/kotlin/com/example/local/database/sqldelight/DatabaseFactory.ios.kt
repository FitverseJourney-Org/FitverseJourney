package com.example.local.database.sqldelight

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.journey.database.AppDatabase.AppDatabase

actual class DatabaseFactory {
    actual fun createDriver(): SqlDriver {
        val schema = AppDatabase.Schema.synchronous()
        return NativeSqliteDriver(
            schema  = schema, // ← sem .synchronous()
            name    = "fitverse_v3.db",
            onConfiguration = { config ->
                config.copy(
                    extendedConfig = DatabaseConfiguration.Extended(
                        foreignKeyConstraints = true
                    )
                )
            }
        )
    }
}