package org.fitverse.fitverseJourney

import android.app.Application
import androidx.datastore.dataStore
import com.example.data.di.dataSourcesModules
import com.example.data.di.networkModule
import com.google.firebase.FirebaseApp
import org.fitverse.fitverseJourney.di.dataStoreModule
import org.fitverse.fitverseJourney.di.firebaseModule
import org.fitverse.fitverseJourney.di.repositoriesModule
import org.fitverse.fitverseJourney.di.useCasesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(
                modules = listOf(
                    networkModule,
                    dataStoreModule,
                    dataSourcesModules,
                    firebaseModule,
                    useCasesModule,
                    repositoriesModule
                )
            )
        }

    }
}