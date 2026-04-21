package org.fitverse.fitverseJourney

import android.app.Application
import com.example.local.database.androidLocalPlatformModule
import com.example.local.database.datastore.DataStoreFactory
import com.example.remote.androidRemotePlatformModule
import com.google.firebase.FirebaseApp
import org.fitverse.project.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication : Application() {

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()

        // 1. Passamos o contexto global (Application Context) para o DataStore.
        // Isso resolve completamente o risco de Memory Leak!
        DataStoreFactory.context = this
        initKoin(
            platformModules = listOf(androidLocalPlatformModule,androidRemotePlatformModule)
        ) {
            androidLogger()
            androidContext(this@MainApplication)
        }
    }
}