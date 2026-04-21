package com.example.domain.friends

/**
 * Eventos de UI (user interactions)
 */
sealed interface FriendsEvent {
    data class UpdateFriendCode(val code: String) : FriendsEvent
    data object AddFriendByCode : FriendsEvent
    data object OpenQrScanner : FriendsEvent
    data object ToggleSortOrder : FriendsEvent
    data class AddSuggestion(val userId: String) : FriendsEvent
    data class RemoveFriend(val userId: String) : FriendsEvent
    data object Refresh : FriendsEvent
    data object DismissError : FriendsEvent
}