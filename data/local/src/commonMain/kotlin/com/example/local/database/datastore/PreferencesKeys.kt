package com.example.local.database.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {

    val IS_AUTHENTICATED = booleanPreferencesKey("is_authenticated")
    val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    val IS_TRIAL_COMPLETED = booleanPreferencesKey("is_trial_completed")
    val APP_LANGUAGE = stringPreferencesKey("app_language")

}