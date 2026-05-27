package org.fitverse.data.local.database.sqldelight

import com.journey.AppDatabase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DatabaseSqlDeLightHelper(
    private val driverFactory: DatabaseFactory,
) {
    private var db: AppDatabase? = null
    private val mutex = Mutex()

    suspend fun <Result : Any> withDatabase(block: suspend (AppDatabase) -> Result?): Result? =
        mutex.withLock {
            if (db == null) {
                db = AppDatabase(driverFactory.createDriver())
            }
            block(db!!)
        }
}
