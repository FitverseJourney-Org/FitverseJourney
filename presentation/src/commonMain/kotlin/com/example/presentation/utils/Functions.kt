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

}


