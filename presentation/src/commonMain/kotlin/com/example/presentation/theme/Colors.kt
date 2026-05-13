package com.example.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val transparent = Color.Transparent

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
    val Border      = Color(0x12FFFFFF)
    val Border2     = Color(0x1FFFFFFF)

    // ── Accents ───────────────────────────────────────────────
    val Accent      = Color(0xFFC8FF00)
    val AccentDim   = Color(0x1FC8FF00)
    val AccentDim2  = Color(0x0FC8FF00)

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
    val Teal        = Color(0xFF00D4B4)  // notificação: desafio
    val TealDim     = Color(0x2600D4B4)
    val Amber       = Color(0xFFFFB830)  // notificação: ranking / liga
    val AmberDim    = Color(0x26FFB830)
    val Gray        = Color(0xFF8890A4)  // notificação: sistema
    val GrayDim     = Color(0x1F8890A4)

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

object FVExtension {
    val bg         = Color(0xFF0A0A0F)
    val surface    = Color(0xFF111118)
    val surface2   = Color(0xFF18181F)
    val surface3   = Color(0xFF1E1E28)
    val primary    = Color(0xFFC8FF00)   // Neon Lime
    val secondary  = Color(0xFF00FFB2)   // Neon Aqua
    val danger     = Color(0xFFFF4D1C)   // Fire Orange
    val xp         = Color(0xFF7C6FFF)   // Nexus Violet
    val gold       = Color(0xFFFFD700)
    val silver     = Color(0xFFA0A0A0)
    val bronze     = Color(0xFFCD7F32)
    val text       = Color(0xFFFFFFFF)
    val textMuted  = Color(0xFF6B7280)
    val textDim    = Color(0xFF9CA3AF)
    val outline    = Color(0xFF2A2A35)
    val margin     = 20.dp
    val radius     = 16.dp
    val radiusBtn  = 12.dp
    val radiusPill = 100.dp
}