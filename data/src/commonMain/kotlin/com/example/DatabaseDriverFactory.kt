package com.example

import app.cash.sqldelight.db.SqlDriver

const val DB_FILE_NAME = "fitversedb.db"

expect class DatabaseDriverFactory {
    suspend fun createDriver() : SqlDriver
}