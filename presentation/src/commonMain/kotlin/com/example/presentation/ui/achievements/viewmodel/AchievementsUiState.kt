package org.fitverse.presentation.ui.achievements.viewmodel

import org.fitverse.domain.repository.dbLocal.sqldelight.achievements.AchievementRecord
import org.fitverse.presentation.ui.achievements.Achievement
import org.fitverse.presentation.ui.achievements.AchievementCategory
import org.fitverse.presentation.ui.achievements.AchievementRarity
import org.fitverse.presentation.ui.achievements.AchievementStatus

data class AchievementsUiState(
    val achievements: List<Achievement> = emptyList(),
    val isLoading: Boolean = true,
    val selectedCategory: AchievementCategory? = null,
    val selectedStatus: AchievementStatus? = null,
    val selectedAchievement: Achievement? = null,
)

sealed interface AchievementsIntent {
    data object NavigateBack : AchievementsIntent
    data class FilterByCategory(val category: AchievementCategory?) : AchievementsIntent
    data class FilterByStatus(val status: AchievementStatus?) : AchievementsIntent
    data class SelectAchievement(val achievement: Achievement?) : AchievementsIntent
}

sealed interface AchievementsEvent {
    data object NavigateBack : AchievementsEvent
}

fun AchievementRecord.toAchievementUi(): Achievement = Achievement(
    id        = id,
    icon      = icon,
    name      = title,
    desc      = description,
    xp        = xpReward,
    rarity    = when (rarity) {
        "RARE"      -> AchievementRarity.RARE
        "EPIC"      -> AchievementRarity.EPIC
        "LEGENDARY" -> AchievementRarity.LEGENDARY
        else        -> AchievementRarity.COMMON
    },
    status    = when (status) {
        "UNLOCKED"    -> AchievementStatus.UNLOCKED
        "IN_PROGRESS" -> AchievementStatus.IN_PROGRESS
        else          -> AchievementStatus.LOCKED
    },
    cat       = when (category) {
        "NUTRICAO"  -> AchievementCategory.NUTRICAO
        "STREAK"    -> AchievementCategory.STREAK
        "SOCIAL"    -> AchievementCategory.SOCIAL
        "ESPECIAIS" -> AchievementCategory.ESPECIAIS
        else        -> AchievementCategory.TREINO
    },
    progress  = progress.toFloat(),
    condition = condition,
    date      = unlockedAt ?: "",
)
