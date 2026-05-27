package org.fitverse.presentation.ui.community.viewmodel

import org.fitverse.presentation.ui.community.Community
import org.fitverse.presentation.ui.community.Post
import org.fitverse.presentation.ui.community.myCommunities
import org.fitverse.presentation.ui.community.samplePosts

data class CommunityUiState(
    val communities:     List<Community> = emptyList(),
    val posts:           List<Post>      = samplePosts,
    val showCreateSheet: Boolean         = false,
    val showJoinSheet:   Boolean         = false,
    val isRefreshing:    Boolean         = false,
)

sealed interface CommunityIntent {
    data object ShowCreateSheet                           : CommunityIntent
    data object ShowJoinSheet                             : CommunityIntent
    data object DismissSheets                             : CommunityIntent
    data class  OpenGroupHome(val groupName: String)      : CommunityIntent
}

sealed interface CommunityEvent {
    data object NavigateToAddPost                         : CommunityEvent
    data class  NavigateToGroupHome(val groupName: String): CommunityEvent
}
