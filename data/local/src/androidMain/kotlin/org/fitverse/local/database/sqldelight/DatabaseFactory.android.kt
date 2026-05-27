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
            name     = "fitverse.db",
            callback = object : AndroidSqliteDriver.Callback(schema) {

                // ✅ onConfigure: chamado ANTES de onCreate/onUpgrade
                // Garante FK ativo durante migrations também
                override fun onConfigure(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    db.setForeignKeyConstraintsEnabled(true)
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    // WAL: melhor performance de escrita concorrente
                    db.execSQL("PRAGMA journal_mode = WAL")
                }
            }
        )
    }

    private fun deleteDatabaseFiles(dbName: String) {
        // arquivo principal
        val db  = context.getDatabasePath(dbName)
        // arquivos auxiliares do WAL — deleteDatabase() NÃO remove esses
        val wal = context.getDatabasePath("$dbName-wal")
        val shm = context.getDatabasePath("$dbName-shm")

        listOf(db, wal, shm).forEach { file ->
            if (file.exists()) {
                val deleted = file.delete()
                println("🗑️ ${file.name} → ${if (deleted) "deletado" else "FALHOU"}")
            }
        }
    }

}