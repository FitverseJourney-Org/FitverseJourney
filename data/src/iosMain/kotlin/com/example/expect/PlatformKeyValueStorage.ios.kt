package com.example.expect

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.Foundation.NSUserDefaults

actual class PlatformKeyValueStorage {

    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun observeString(key: String, defaultValue: String): Flow<String> = flow {
        emit(getString(key, defaultValue))
    }

    actual suspend fun getString(key: String, defaultValue: String): String {
        return defaults.stringForKey(key) ?: defaultValue
    }

    actual suspend fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    actual suspend fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }
}