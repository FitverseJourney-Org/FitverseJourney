package com.example.expect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.dbLocal.datastore.createDataStore


actual fun rememberDataStore(): DataStore<Preferences> {
    return createDataStore()
}