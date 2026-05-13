package com.example.presentation.ui.achievements.viewmodel

import com.example.presentation.ui.achievements.Achievement
import com.example.presentation.ui.achievements.AchievementsData

data class AchievementsUiState(
    val achievements: List<Achievement> = AchievementsData.all,
)

sealed interface AchievementsIntent {
    data object NavigateBack : AchievementsIntent
}

sealed interface AchievementsEvent {
    data object NavigateBack : AchievementsEvent
}
