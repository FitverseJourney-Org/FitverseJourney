package org.fitverse.presentation.ui.community.viewmodel

import org.fitverse.presentation.ui.community.GroupMission
import org.fitverse.presentation.ui.community.GroupPost
import org.fitverse.presentation.ui.community.GroupSeason
import org.fitverse.presentation.ui.community.mockFixedMissions
import org.fitverse.presentation.ui.community.mockGroupPosts
import org.fitverse.presentation.ui.community.mockSurpriseMission

enum class GroupTab(val label: String) {
    POSTS("POSTS"),
    MISSIONS("MISSÕES"),
    RANKING("RANKING"),
    GLOBAL("GLOBAL"),
}

data class GroupHomeUiState(
    val groupName: String = "Warriors GYM",
    val season: GroupSeason = GroupSeason(
        name       = "Season 3 — Iron Warriors",
        daysLeft   = 18,
        currentXp  = 42_380,
        targetXp   = 75_000,
        milestones = listOf(10_000, 35_000, 40_000, 66_000, 75_000),
    ),
    val selectedTab:          GroupTab           = GroupTab.POSTS,
    val todayXp:              Int                = 300,
    val completedMissions:    Int                = 2,
    val totalMissions:        Int                = 4,
    val fixedMissions:        List<GroupMission>  = mockFixedMissions,
    val surpriseMission:      GroupMission?       = mockSurpriseMission,
    val participationPercent: Int                = 73,
    val activeMembers:        Int                = 94,
    val totalMembers:         Int                = 128,
    val resetTime:            String             = "06h 22min",
    val xpCollected:          Boolean            = false,
    val groupPosts:           List<GroupPost>    = mockGroupPosts,
)

sealed interface GroupHomeIntent {
    data class  SelectTab(val tab: GroupTab)      : GroupHomeIntent
    data object CollectXp                         : GroupHomeIntent
    data class  ToggleLike(val postId: String)    : GroupHomeIntent
}

sealed interface GroupHomeEvent {
    data object NavigateBack : GroupHomeEvent
}
