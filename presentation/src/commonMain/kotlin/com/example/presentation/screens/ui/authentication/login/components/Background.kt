package com.example.presentation.screens.ui.authentication.login.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun AnimatedLoginBackground(
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val infiniteTransition = rememberInfiniteTransition()

    // Movimento vertical suave (respiração)
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 9_000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Pulsação leve de energia (usa cor secundária da paleta)
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.04f,
        targetValue = 0.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5_000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // fundo base: gradiente usando background -> surface -> surfaceVariant
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    cs.background,
                    cs.surface,
                    cs.surfaceVariant
                ),
                startY = 0f,
                endY = h
            )
        )

        // onda orgânica (suave) — usa primaryContainer para um tom coeso
        val wavePath = Path().apply {
            val amplitude = h * 0.11f
            val centerY = h * (0.72f + waveOffset * 0.04f)

            moveTo(0f, centerY)
            cubicTo(
                w * 0.22f, centerY - amplitude,
                w * 0.55f, centerY + amplitude,
                w, centerY
            )
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }

        drawPath(
            path = wavePath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    cs.primaryContainer.copy(alpha = 0.10f),
                    cs.primaryContainer.copy(alpha = 0.02f),
                    androidx.compose.ui.graphics.Color.Transparent
                )
            )
        )

        // glow radial de energia usando secondary (accent)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    cs.secondary.copy(alpha = glowAlpha),
                    androidx.compose.ui.graphics.Color.Transparent
                )
            ),
            radius = w * 0.9f,
            center = Offset(x = w / 2f, y = h * 0.26f)
        )

        // um segundo glow mais discreto próximo à base (para profundidade)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    cs.primary.copy(alpha = 0.03f),
                    androidx.compose.ui.graphics.Color.Transparent
                )
            ),
            radius = w * 0.6f,
            center = Offset(x = w * 0.22f, y = h * 0.78f)
        )
    }
}