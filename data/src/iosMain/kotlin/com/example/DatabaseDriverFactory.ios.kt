package com.example

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.journey.fitverse.fitverse_db

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
       return NativeSqliteDriver(
           schema = fitverse_db.Schema.synchronous(),
           name = DB_FILE_NAME,
           onConfiguration = { config: DatabaseConfiguration ->
               config.copy(
                   extendedConfig = DatabaseConfiguration.Extended(
                       foreignKeyConstraints = true
                   )
               )
           }
       )
    }
}