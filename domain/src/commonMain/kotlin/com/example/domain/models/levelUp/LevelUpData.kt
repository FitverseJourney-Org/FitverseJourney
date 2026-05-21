package org.fitverse.domain.models.levelUp

/**
 * All the data needed to render the LevelUp celebration screen.
 */
data class LevelUpData(
    val userName: String  = "ALEX RIVERS",
    val level: Int        = 24,
    val className: String = "WARRIOR CLASS",
    val xpGained: Int     = 200,
    val badgeLabel: String= "New Badge",
)
