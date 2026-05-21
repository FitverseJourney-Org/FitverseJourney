package org.fitverse.presentation.ui.friends.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.domain.models.friends.FriendsError
import org.fitverse.domain.models.friends.FriendsEvent
import org.fitverse.domain.models.friends.FriendsUiState
import org.fitverse.domain.models.friends.SortOrder
import org.fitverse.domain.models.dashboard.UserProfile
import org.fitverse.domain.repository.FriendsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel com Dependency Injection e Unidirectional Data Flow
 */

class FriendsViewModel(
    private val friendsRepository: FriendsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendsUiState())
    val uiState: StateFlow<FriendsUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    /**
     * Manipula todos os eventos de UI em um único ponto
     */
    fun onEvent(event: FriendsEvent) {
        when (event) {
            is FriendsEvent.UpdateFriendCode -> updateFriendCode(event.code)
            is FriendsEvent.AddFriendByCode -> addFriendByCode()
            is FriendsEvent.ToggleSortOrder -> toggleSortOrder()
            is FriendsEvent.AddSuggestion -> addSuggestion(event.userId)
            is FriendsEvent.RemoveFriend -> removeFriend(event.userId)
            is FriendsEvent.Refresh -> refresh()
            is FriendsEvent.DismissError -> dismissError()
            is FriendsEvent.OpenQrScanner -> { /* Delegado para Navigation */ }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Carregamento paralelo usando combine
                combine(
                    friendsRepository.getFriends(),
                    friendsRepository.getSuggestions()
                ) { friends, suggestions ->
                    friends to suggestions
                }.collect { (friends, suggestions) ->
                    _uiState.update { state ->
                        state.copy(
                            friends = friends.sortedBy(state.sortOrder),
                            suggestions = suggestions,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = FriendsError.NetworkError(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }

    private fun updateFriendCode(code: String) {
        _uiState.update { it.copy(friendCode = code.uppercase().trim()) }
    }

    private fun addFriendByCode() {
        val code = _uiState.value.friendCode

        if (!isValidFriendCode(code)) {
            _uiState.update {
                it.copy(error = FriendsError.InvalidCode("Código inválido. Use o formato FIT-XXXX"))
            }
            return
        }

        viewModelScope.launch {
            try {
                friendsRepository.addFriendByCode(code)
                _uiState.update { it.copy(friendCode = "", error = null) }
                // Trigger success feedback (via SharedFlow se necessário)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = FriendsError.UserNotFound(code))
                }
            }
        }
    }

    private fun toggleSortOrder() {
        _uiState.update { state ->
            val newOrder = state.sortOrder.toggle()
            state.copy(
                sortOrder = newOrder,
                friends = state.friends.sortedBy(newOrder)
            )
        }
    }

    private fun addSuggestion(userId: String) {
        viewModelScope.launch {
            try {
                friendsRepository.addFriend(userId)
                // Otimistic UI update
                _uiState.update { state ->
                    state.copy(
                        suggestions = state.suggestions.filterNot { it.id == userId }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = FriendsError.NetworkError(e.message ?: "")) }
            }
        }
    }

    private fun removeFriend(userId: String) {
        viewModelScope.launch {
            try {
                friendsRepository.removeFriend(userId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = FriendsError.NetworkError(e.message ?: "")) }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            loadInitialData()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun isValidFriendCode(code: String): Boolean {
        return code.matches(Regex("^FIT-\\d{4}$"))
    }

    private fun List<UserProfile>.sortedBy(order: SortOrder): List<UserProfile> {
        return when (order) {
            SortOrder.ASCENDING -> this.sortedBy { it.name }
            SortOrder.DESCENDING -> this.sortedByDescending { it.name }
        }
    }
}