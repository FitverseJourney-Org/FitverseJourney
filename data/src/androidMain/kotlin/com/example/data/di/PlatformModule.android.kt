package com.example.data.di

import com.example.data.database.sqldelight.DatabaseFactory
import com.example.data.util.AndroidNetworkMonitor
import com.example.data.util.NetworkMonitor
import com.example.datasource.FirebaseAuthDataSource
import com.example.domain.repository.authentication.AuthRemoteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidPlatformModule = module {
    // DatabaseFactory
    single { DatabaseFactory(androidContext()) }

    // NetworkMonitor
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }

    single<AuthRemoteRepository>{
        FirebaseAuthDataSource(
            auth = get()
        )
    }
    single<FirebaseAuth>{ FirebaseAuth.getInstance() }

    single<FirebaseUser?>{ FirebaseAuth.getInstance().currentUser }

    // PreferencesDataSource (Android DataStore)
    // single<PreferencesDataSource> { AndroidPreferencesDataSource(androidContext()) }
}
