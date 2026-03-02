package com.example.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val GamifiedDarkColorScheme: ColorScheme = darkColorScheme(
    primary = DarkGamifiedColors.Primary,
    onPrimary = DarkGamifiedColors.Title,
    primaryContainer = DarkGamifiedColors.PrimarySoft,
    onPrimaryContainer = DarkGamifiedColors.Title,

    secondary = DarkGamifiedColors.Accent,
    onSecondary = DarkGamifiedColors.Title,

    background = DarkGamifiedColors.Background,
    onBackground = DarkGamifiedColors.Body,

    surface = DarkGamifiedColors.Surface,
    onSurface = DarkGamifiedColors.Body,
    surfaceVariant = DarkGamifiedColors.Card,

    error = DarkGamifiedColors.Health,
    onError = DarkGamifiedColors.Title,

    outline = DarkGamifiedColors.Outline
)

private val AppTypography = Typography(
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, fontFamily = FontFamily.Default),
    bodyMedium = TextStyle(fontSize = 14.sp),
    bodySmall = TextStyle(fontSize = 12.sp),
    titleLarge = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    labelLarge = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
)

@Composable
fun GamifiedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // for now only dark scheme provided; extend for light if needed
    val colors = GamifiedDarkColorScheme
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}