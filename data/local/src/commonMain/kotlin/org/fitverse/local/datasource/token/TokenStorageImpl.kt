package org.fitverse.data.local.datasource.token

import org.fitverse.domain.models.token.TokenProvider
import com.russhwolf.settings.Settings


class TokenStorageImpl(
    private val settings: Settings
) : TokenProvider {
    private companion object {
        const val KEY_TOKEN = "jwt_token"
    }

    override fun saveToken(token: String) {
        settings.putString(KEY_TOKEN, token)
    }

    override fun getToken(): String? {
        return settings.getStringOrNull(KEY_TOKEN)
    }

    override fun clearToken() {
        settings.remove(KEY_TOKEN)
    }

    fun hasToken(): Boolean {
        return settings.hasKey(KEY_TOKEN)
    }

}