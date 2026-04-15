package com.example.data.di

import com.example.data.database.sqldelight.DatabaseFactory
import com.example.data.util.IOSNetworkMonitor
import com.example.data.util.NetworkMonitor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val iosPlatformModule = module {
    // DatabaseFactory
    singleOf(::DatabaseFactory)

    // NetworkMonitor
    single<NetworkMonitor> { IOSNetworkMonitor() }

    // PreferencesDataSource (iOS UserDefaults)
    // single<PreferencesDataSource> { IOSPreferencesDataSource() }
}