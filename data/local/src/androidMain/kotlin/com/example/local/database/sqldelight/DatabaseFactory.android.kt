package org.fitverse.data.local.database.sqldelight

import app.cash.sqldelight.db.SqlDriver
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.journey.AppDatabase

actual class DatabaseFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        val schema = AppDatabase.Schema.synchronous()
        return AndroidSqliteDriver(
            schema   = schema,
            context  = context,
            name     = "fitverse_v3.db",
            callback = object : AndroidSqliteDriver.Callback(schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
    }
}