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

    // ── Backgrounds ───────────────────────────────────────────
    val Bg          = Color(0xFF0E0E12)
    val Surface     = Color(0xFF16161E)
    val Surface2    = Color(0xFF1E1E28)
    val SurfaceCard = Color(0xFF1C1C26)

    // ── Borders ───────────────────────────────────────────────
    val Border      = Color(0x12FFFFFF)   // 7% white
    val Border2     = Color(0x1FFFFFFF)   // 12% white

    // ── Accents ───────────────────────────────────────────────
    val Accent      = Color(0xFFC8FF00)   // neon yellow-green
    val AccentDim   = Color(0x1FC8FF00)   // 12%
    val AccentDim2  = Color(0x0FC8FF00)   // 6%

    val Green       = Color(0xFF00E87A)
    val GreenDim    = Color(0x1F00E87A)
    val Orange      = Color(0xFFFF6B35)
    val OrangeDim   = Color(0x26FF6B35)
    val Purple      = Color(0xFF9D6FFF)
    val PurpleDim   = Color(0x269D6FFF)
    val Blue        = Color(0xFF3B82F6)
    val BlueDim     = Color(0x1F3B82F6)
    val Red         = Color(0xFFFF4757)
    val RedDim      = Color(0x1FFF4757)

    // ── Text ──────────────────────────────────────────────────
    val TextPrimary = Color(0xFFF0F0F8)
    val TextMuted   = Color(0xFF888899)
    val TextMuted2  = Color(0xFF555566)

    // ── Tag colors (Wiki) ─────────────────────────────────────
    val TagForca    = Accent
    val TagTreino   = Green
    val TagNutricao = Purple
    val TagRecovery = Red
    val TagCardio   = Orange
}