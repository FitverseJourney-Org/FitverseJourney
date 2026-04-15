package com.example.data.database.sqldelight

import app.cash.sqldelight.db.SqlDriver
import com.journey.database.AppDatabase

expect class DatabaseFactory {
    fun createDriver(): SqlDriver
}

fun DatabaseFactory.createDatabase(): AppDatabase {
    return AppDatabase(createDriver())
}