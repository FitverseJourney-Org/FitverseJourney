package org.fitverse.fitverseJourney

import android.app.Application
import org.fitverse.data.local.di.androidLocalPlatformModule
import org.fitverse.data.remote.androidRemotePlatformModule
import com.google.firebase.FirebaseApp
import org.fitverse.project.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication : Application() {

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()

        initKoin(
            platformModules = listOf(androidLocalPlatformModule,androidRemotePlatformModule)
        ) {
            androidLogger()
            androidContext(this@MainApplication)
        }
    }
}