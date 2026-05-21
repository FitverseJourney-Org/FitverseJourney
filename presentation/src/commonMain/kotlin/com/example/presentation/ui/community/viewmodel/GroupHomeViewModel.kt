package org.fitverse.presentation.ui.community.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupHomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GroupHomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<GroupHomeEvent>()
    val events = _events.receiveAsFlow()

    fun setGroupName(name: String) {
        _uiState.update { it.copy(groupName = name) }
    }

    fun onIntent(intent: GroupHomeIntent) {
        when (intent) {
            is GroupHomeIntent.SelectTab  -> _uiState.update { it.copy(selectedTab = intent.tab) }
            GroupHomeIntent.CollectXp    -> _uiState.update { it.copy(xpCollected = true) }
            is GroupHomeIntent.ToggleLike -> _uiState.update { state ->
                state.copy(
                    groupPosts = state.groupPosts.map { post ->
                        if (post.id == intent.postId) post.copy(
                            isLiked = !post.isLiked,
                            likes   = if (post.isLiked) post.likes - 1 else post.likes + 1,
                        ) else post
                    }
                )
            }
        }
    }

    fun navigateBack() {
        viewModelScope.launch { _events.send(GroupHomeEvent.NavigateBack) }
    }
}
