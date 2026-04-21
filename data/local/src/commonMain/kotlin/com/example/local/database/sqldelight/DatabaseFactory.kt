package com.example.local.database.sqldelight

import app.cash.sqldelight.db.SqlDriver
import com.journey.database.AppDatabase.AppDatabase

expect class DatabaseFactory {
    fun createDriver(): SqlDriver
}

fun DatabaseFactory.createDatabase(): AppDatabase {
    return AppDatabase(createDriver())
}