package org.fitverse.presentation.ui.community.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<CommunityEvent>()
    val events = _events.receiveAsFlow()

    fun onIntent(intent: CommunityIntent) {
        when (intent) {
            CommunityIntent.ShowCreateSheet     -> _uiState.update { it.copy(showCreateSheet = true,  showJoinSheet = false) }
            CommunityIntent.ShowJoinSheet       -> _uiState.update { it.copy(showJoinSheet   = true,  showCreateSheet = false) }
            CommunityIntent.DismissSheets       -> _uiState.update { it.copy(showCreateSheet = false, showJoinSheet = false) }
            is CommunityIntent.OpenGroupHome    -> navigateToGroupHome(intent.groupName)
        }
    }

    fun navigateToAddPost() {
        viewModelScope.launch { _events.send(CommunityEvent.NavigateToAddPost) }
    }

    private fun navigateToGroupHome(groupName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(showCreateSheet = false, showJoinSheet = false) }
            _events.send(CommunityEvent.NavigateToGroupHome(groupName))
        }
    }
}
