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
    // --- BACKGROUNDS & SURFACES ---

    // Fundo principal do app (OLED Black)
    val Background = Color(0xFF0a0a0f)

    // Ideal para Cards, Bottom Sheets e Navigation Bars
    val Surface = Color(0xFF111118)

    // --- BRAND COLORS (Ações e Gamificação) ---

    // Logo, Splash Screen, Botão Primário e ícone de "Home"
    val Primary = Color(0xFFc8ff00)

    // Containers de seleção, hover states e fundo de "Quick Commands"
    val PrimarySoft = Color(0xFF2D1B59)

    // Plano Trial, Barra de Hidratação e progresso secundário
    val Secondary = Color(0xFF00FFB2)

    // Plano Pro, Botão "Become a Pro" e indicadores de Saúde/Stamina
    val Tertiary = Color(0xFF10B981)

    // --- TEXTO & DETALHES ---

    // Títulos de Cards e Headlines principais
    val OnBackground = Color(0xFFFFFFFF)

    val NeutralMuted = Color(0xFF94A3B8)

    val DeepNeutral = Color(0xFF0A0B0F)

    // Legendas, unidades (BPM, kg) e textos de apoio "muted"
    val OnSurfaceVariant = Color(0xFF71717A)

    // Divisores de seção e bordas de inputs
    val Outline = Color(0xFF2C2C2E)
}

object FitverseColors {
    val BgDark      = Color(0xFF0C0C0C)
    val Surface1    = Color(0xFF181818)
    val Surface2    = Color(0xFF202020)
    val Accent      = Color(0xFFCDFF00)
    val AccentDim   = Color(0xFF8EAF00)
    val Purple      = Color(0xFF8B5CF6)
    val Blue        = Color(0xFF3B82F6)
    val Orange      = Color(0xFFF97316)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextMuted   = Color(0xFF888888)
    val TextDim     = Color(0xFF333333)
}