package com.example.presentation.ui.community.viewmodel

import com.example.presentation.ui.community.Community
import com.example.presentation.ui.community.Post
import com.example.presentation.ui.community.myCommunities
import com.example.presentation.ui.community.samplePosts

data class CommunityUiState(
    val communities:     List<Community> = myCommunities,
    val posts:           List<Post>      = samplePosts,
    val showCreateSheet: Boolean         = false,
    val showJoinSheet:   Boolean         = false,
)

sealed interface CommunityIntent {
    data object ShowCreateSheet : CommunityIntent
    data object ShowJoinSheet   : CommunityIntent
    data object DismissSheets   : CommunityIntent
}

sealed interface CommunityEvent {
    data object NavigateToAddPost : CommunityEvent
}
