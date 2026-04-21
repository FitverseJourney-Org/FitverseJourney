package com.example.local.database.datastore

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@SuppressLint("StaticFieldLeak")
actual object DataStoreFactory {

    lateinit var context: Context

    actual val dataStore: DataStore<Preferences> by lazy {
        createDataStore()
    }

    actual fun createDataStore(): DataStore<Preferences> {
        return com.example.local.expect.createDataStore(
            producePath = {
                context.filesDir.resolve(dataStoreFileName).absolutePath
            }
        )
    }
}