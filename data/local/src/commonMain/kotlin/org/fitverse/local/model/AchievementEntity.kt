package org.fitverse.data.local.model

data class AchievementEntity(
    val id: String,
    val userId: String,
    val icon: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val rarity: String,      // COMMON, RARE, EPIC, LEGENDARY
    val status: String,      // LOCKED, IN_PROGRESS, UNLOCKED
    val category: String,    // TREINO, NUTRICAO, STREAK, SOCIAL, ESPECIAIS
    val progress: Double,    // 0.0 – 1.0
    val maxProgress: Double,
    val condition: String,
    val unlockedAt: String?, // data display, nullable
)
