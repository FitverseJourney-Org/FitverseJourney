package com.example.local.database.sqldelight

import com.journey.database.AppDatabase.AppDatabase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DatabaseSqlDeLightHelper(
    private val driverFactory: DatabaseFactory
) {
    private var db: AppDatabase? = null

    private val mutex = Mutex()

    suspend fun <Result: Any> withDatabase(block : suspend (AppDatabase) -> Result?) : Result?{
        return mutex.withLock {

            if(db == null){
                db = AppDatabase.Companion(driverFactory.createDriver())
            }


            return@withLock block(db!!)
        }
    }
}