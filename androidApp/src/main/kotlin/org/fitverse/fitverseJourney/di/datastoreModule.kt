package org.fitverse.fitverseJourney.di


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.data.dbLocal.repositories.LanguageRepositoryImpl
import com.example.domain.repository.LanguageRepository
import com.example.domain.usecase.datastore.LanguageUseCase
import com.example.expect.PlatformKeyValueStorage
import com.example.presentation.presenter.AppPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {

    // DataStore (infra)
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile("settings.preferences_pb")
        }
    }
    // expect/actual resolvido automaticamente
    single {
        PlatformKeyValueStorage(
            dataStore = get()
        )
    }
    single<LanguageRepository> {
        LanguageRepositoryImpl(storage = get())
    }

    single { LanguageUseCase(get()) }
    single { AppPresenter(get()) }

}