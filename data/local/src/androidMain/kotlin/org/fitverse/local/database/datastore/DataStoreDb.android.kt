package org.fitverse.data.local.database.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual object DataStoreFactory {
    actual val dataStore: DataStore<Preferences>
        get() = error("Inject DataStore<Preferences> via Koin — do not call DataStoreFactory.dataStore directly on Android")

    actual fun createDataStore(): DataStore<Preferences> =
        error("Inject DataStore<Preferences> via Koin — do not call DataStoreFactory.createDataStore() directly on Android")
}