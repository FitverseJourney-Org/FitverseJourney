package org.fitverse.data.local.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.fitverse.data.local.database.datastore.dataStoreFileName
import org.fitverse.data.local.database.sqldelight.DatabaseFactory
import org.fitverse.data.local.expect.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

val iosLocalPlatformModule = module {
    singleOf(::DatabaseFactory)
    single<DataStore<Preferences>> {
        @OptIn(ExperimentalForeignApi::class)
        createDataStore {
            val dir = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            requireNotNull(dir).path + "/$dataStoreFileName"
        }
    }
}