package com.example.data.dbLocal.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createDatastorePatch(
    producePatch: () -> String,
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath {
        producePatch().toPath()
    }
}