package com.example.data.di

import com.example.data.database.sqldelight.DatabaseFactory
import com.example.data.util.AndroidNetworkMonitor
import com.example.data.util.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidPlatformModule = module {
    // DatabaseFactory
    single { DatabaseFactory(androidContext()) }

    // NetworkMonitor
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }

    // PreferencesDataSource (Android DataStore)
    // single<PreferencesDataSource> { AndroidPreferencesDataSource(androidContext()) }
}
