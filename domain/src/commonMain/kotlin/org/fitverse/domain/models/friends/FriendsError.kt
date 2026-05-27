package org.fitverse.domain.models.friends

/**
 * Sealed interface para erros tipados
 */
sealed interface FriendsError {
    data class InvalidCode(val message: String) : FriendsError
    data class NetworkError(val message: String) : FriendsError
    data class UserNotFound(val code: String) : FriendsError
    data object Unknown : FriendsError
}