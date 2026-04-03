package org.fitverse.fitverseJourney

import android.app.Application
import com.example.expect.AppDataStoreDb
import com.google.firebase.FirebaseApp
import org.fitverse.fitverseJourney.di.appModule
import org.fitverse.fitverseJourney.di.firebaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()

        // 1. Passamos o contexto global (Application Context) para o DataStore.
        // Isso resolve completamente o risco de Memory Leak!
        AppDataStoreDb.context = this

        startKoin {
            androidContext(this@MainApplication)
            modules(appModule + firebaseModule)
        }

    }
}