package com.example.presentation.utils

import kotlin.math.max


object Dashboard {
    fun xpForLevel(level: Int): Int {
        val base = 100
        return base * level * level
    }

    fun levelFromXp(totalXp: Int): Int {
        var lvl = 1
        while (totalXp >= xpForLevel(lvl)) lvl++
        return max(1, lvl)
    }

    fun xpFractionForCurrentLevel(totalXp: Int, level: Int): Float {
        val requiredForPrevious = if (level > 1) xpForLevel(
            level - 1
        ) else 0
        val requiredForCurrent = xpForLevel(level)
        val earned = totalXp - requiredForPrevious
        return (earned.toFloat() / (requiredForCurrent - requiredForPrevious)).coerceIn(0f, 1f)
    }
}


