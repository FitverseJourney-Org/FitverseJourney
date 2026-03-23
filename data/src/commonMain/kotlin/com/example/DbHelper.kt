package com.example

import com.journey.fitverse.fitverse_db
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DbHelper(
    private val driverFactory: DatabaseDriverFactory
) {
    private var db: fitverse_db? = null

    private val mutex = Mutex()

    suspend fun <Result: Any> withDatabase(block : suspend (fitverse_db) -> Result?) : Result?{
        return mutex.withLock {

            if(db == null){
                db = fitverse_db(driverFactory.createDriver())
            }


            return@withLock block(db!!)
        }
    }
}