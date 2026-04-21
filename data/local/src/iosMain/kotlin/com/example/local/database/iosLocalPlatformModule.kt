package com.example.local.database

import com.example.local.database.sqldelight.DatabaseFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val iosLocalPlatformModule = module {
    singleOf(::DatabaseFactory)
}