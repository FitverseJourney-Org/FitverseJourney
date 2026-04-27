package com.example.domain.models.friends

import androidx.compose.runtime.Immutable

/**
 * Modelo de domínio imutável para perfil de usuário
 * @Immutable garante otimização de recomposição
 */
@Immutable
data class UserProfile(
    val id: String,
    val name: String,
    val username: String,
    val avatarUrl: String? = null,
    val mutualConnections: Int = 0,
    val isFollowing: Boolean = false,
    val streak: Int = 0,
    val lastActiveAt: Long? = null
) {
    val initials: String
        get() = name.split(" ")
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .take(2)
            .joinToString("")
            .ifEmpty { "?" }
}