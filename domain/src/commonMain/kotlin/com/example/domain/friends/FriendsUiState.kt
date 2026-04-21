package com.example.domain.friends

import androidx.compose.runtime.Immutable

/**
 * Estado da UI seguindo padrão UiState
 */
@Immutable
data class FriendsUiState(
    val friends: List<UserProfile> = emptyList(),
    val suggestions: List<UserProfile> = emptyList(),
    val friendCode: String = "",
    val sortOrder: SortOrder = SortOrder.ASCENDING,
    val isLoading: Boolean = false,
    val error: FriendsError? = null,
    val isRefreshing: Boolean = false
)
