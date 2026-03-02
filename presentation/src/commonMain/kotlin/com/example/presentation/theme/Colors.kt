package com.example.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val transparent = Color.Transparent

val backgroundBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF102110),Color(0xFF102110),Color(0xFF102110),Color(0xFF0B180B),Color(0xFF0B180B),Color(0xFF09130E), Color(0xFF000000)),
)



val BaseGreen = Color(0xFF0A160C)
val DeepGreen = Color(0xFF0F2A17)
val AccentGreen = Color(0xFF3FAE6A)
val SoftMint = Color(0xFFB9F6CA)
val SurfaceGreen = Color(0xFF122E1A)
val OnSurfaceText = Color(0xFFE8F5E9)
val DangerRed = Color(0xFFB00020)
val StaminaYellow = Color(0xFFFFC107)
val CardBgDefaultColor = Color(0xFF0F2A17)
val HealthRed = Color(0xFFE57373)


// trial page colors
val TrialBaseGreen = Color(0xFF07120A)
val TrialDeepGreen = Color(0xFF0E2A16)
val TrialAccentGreen = Color(0xFF3FAE6A)
val TrialSurfaceGreen = Color(0xFF122916)
val TrialTextPrimary = Color.White
val TrialTextSecondary = Color(0xFFBFCFC0)


object DarkGamifiedColors {

    // Backgrounds
    val Background = Color(0xFF0E0B16)
    val Surface = Color(0xFF17121E)
    val Card = Color(0xFF1C1626)

    // Text
    val Title = Color(0xFFF5F3FF)
    val Body = Color(0xFFEDE9FE)
    val Subtitle = Color(0xFFBDB4E6)
    val Disabled = Color(0xFF7C77A6)

    // Primary / Accent
    val Primary = Color(0xFFA78BFA)        // roxo premium
    val PrimarySoft = Color(0xFF4C1D95)
    val Accent = Color(0xFF22D3EE)         // contraste moderno

    // Status
    val Health = Color(0xFFF87171)
    val Stamina = Color(0xFF6EE7B7)
    val Xp = Color(0xFFFACC15)             // XP dourado acessível

    // UI helpers
    val Divider = Color(0xFF2E2442)
    val Outline = Color(0xFF3F2E5E)
}

object GamifiedBackgroundBrushes {

    /** Versão levemente mais clara — ideal para dashboard e hero areas */
    val SoftDepth: Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to DarkGamifiedColors.Background,
            0.55f to DarkGamifiedColors.Surface,
            1.0f to DarkGamifiedColors.Card
        )
    )

    /** Versão mais escura — ideal para listas e telas densas */
    val DeepContrast: Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to DarkGamifiedColors.Background,
            0.75f to DarkGamifiedColors.Surface,
            1.0f to DarkGamifiedColors.Surface
        )
    )
}