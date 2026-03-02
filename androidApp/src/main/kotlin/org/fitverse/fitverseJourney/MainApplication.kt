package org.fitverse.fitverseJourney

import android.app.Application
import com.google.firebase.FirebaseApp
import org.fitverse.fitverseJourney.di.firebaseModule
import org.fitverse.project.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(appModule + firebaseModule)
        }

    }
}