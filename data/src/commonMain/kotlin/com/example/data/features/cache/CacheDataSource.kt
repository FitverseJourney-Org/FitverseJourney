import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.domain.repository.cache.CacheDataSource
import kotlinx.coroutines.flow.first
import kotlinx.io.IOException

class CacheDataSourceImpl(
    private val dataStore: DataStore<Preferences>
) : CacheDataSource {

    private companion object {
        val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    override suspend fun saveAuthToken(token: String) {
        dataStore.edit { prefs ->
            prefs[KEY_AUTH_TOKEN] = token
        }
    }

    override suspend fun getAuthToken(): String? {
        return try {
            val prefs = dataStore.data.first()
            prefs[KEY_AUTH_TOKEN]
        } catch (e: IOException) {
            // falha de I/O — retornar nulo é uma escolha segura; logue se necessário
            null
        }
    }

    override suspend fun saveLanguage(language: String) {
        dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language
        }
    }

    override suspend fun getLanguage(): String? {
        return try {
            val prefs = dataStore.data.first()
            prefs[KEY_LANGUAGE]
        } catch (e: IOException) {
            null
        }
    }

    override suspend fun setOnboardingCompleted(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_COMPLETED] = value
        }
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return try {
            val prefs = dataStore.data.first()
            prefs[KEY_ONBOARDING_COMPLETED] ?: false
        } catch (e: IOException) {
            false
        }
    }
}