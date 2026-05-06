package com.example.presentation.ui.authentication.register.states

enum class HeightUnit(val label: String) {
    CM("cm"), FT("ft");

    fun convert(value: Double, to: HeightUnit): Double = when {
        this == CM && to == FT -> value / 30.48
        this == FT && to == CM -> value * 30.48
        else -> value
    }
}