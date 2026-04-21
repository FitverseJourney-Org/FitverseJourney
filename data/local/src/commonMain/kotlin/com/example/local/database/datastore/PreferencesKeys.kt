package com.example.local.database.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {

    val IS_AUTHENTICATED = booleanPreferencesKey("is_authenticated")
    val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    val APP_LANGUAGE = stringPreferencesKey("app_language")
    val USER_TOKEN = stringPreferencesKey("user_token")
}