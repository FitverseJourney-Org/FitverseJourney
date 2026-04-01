package com.example.expect

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

expect object AppDataStoreDb {
    val dataStore: DataStore<Preferences>

    fun createDataStore(): DataStore<Preferences>
}

fun createDataStore(
    producePath: () -> String
) : DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            producePath().toPath()
        }
    )
}

internal const val dataStoreFileName = "fitverse.preferences_pb"