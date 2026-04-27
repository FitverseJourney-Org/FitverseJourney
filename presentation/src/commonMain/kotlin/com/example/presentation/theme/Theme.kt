package com.example.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fitversejourneyapp.presentation.generated.resources.Res
import org.jetbrains.compose.resources.Font


private val AppTypography = Typography(
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, fontFamily = FontFamily.Default),
    bodyMedium = TextStyle(fontSize = 14.sp),
    bodySmall = TextStyle(fontSize = 12.sp),
    titleLarge = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    labelLarge = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
)

@Composable
fun FitVerseJourneyTheme(content: @Composable () -> Unit) {
    val darkColorScheme = darkColorScheme(
        // Primary - Roxo Neon (#7C3AED)
        primary = DarkGamifiedColors.Primary,
        onPrimary = Color.White,
        primaryContainer = DarkGamifiedColors.PrimarySoft,
        onPrimaryContainer = Color.White,

        // Secondary - Azul Elétrico (#2563EB)
        secondary = DarkGamifiedColors.Secondary,
        onSecondary = Color.White,
        secondaryContainer = DarkGamifiedColors.Secondary.copy(alpha = 0.2f),

        // Tertiary - Verde Saúde/Stamina (#10B981)
        tertiary = DarkGamifiedColors.Tertiary,
        onTertiary = Color.Black,
        tertiaryContainer = DarkGamifiedColors.Tertiary.copy(alpha = 0.2f),

        // Neutrals - Ajustados para as referências corretas
        background = DarkGamifiedColors.Background, // DeepNeutral (#0A0B0F)
        onBackground = DarkGamifiedColors.OnBackground,

        surface = DarkGamifiedColors.Surface, // Surface (#16171D)
        onSurface = DarkGamifiedColors.OnBackground,

        // Importante: surfaceVariant é usado para cards e backgrounds de inputs
        surfaceVariant = DarkGamifiedColors.Surface,
        onSurfaceVariant = DarkGamifiedColors.OnSurfaceVariant,

        outline = DarkGamifiedColors.Outline
    )

    MaterialTheme(
        colorScheme = darkColorScheme,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(28.dp) // Curvatura visível nos cards de plano
        ),
        typography = AppTypography,
        content = content
    )
}

