package com.example.expect

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@SuppressLint("StaticFieldLeak")
actual object AppDataStoreDb {

    lateinit var context: Context

    actual val dataStore: DataStore<Preferences> by lazy {
        createDataStore()
    }

    actual fun createDataStore(): DataStore<Preferences> {
        return createDataStore(
            producePath = {
                context.filesDir.resolve(dataStoreFileName).absolutePath
            }
        )
    }
}