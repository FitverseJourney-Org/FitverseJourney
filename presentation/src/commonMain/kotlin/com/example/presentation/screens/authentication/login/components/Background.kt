package com.example.presentation.screens.authentication.login.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

private val BaseGreen = Color(0xFF0A160C)
private val DeepGreen = Color(0xFF0F2A17)
private val AccentGreen = Color(0xFF3FAE6A)

@Composable
fun AnimatedLoginBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg")

    // Movimento vertical suave (respiração)
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 9000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveOffset"
    )

    // Pulsação leve de energia
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val width = size.width
        val height = size.height

        /* ===============================
           FUNDO BASE (gradiente)
           =============================== */
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    _root_ide_package_.com.example.presentation.screens.authentication.login.components.BaseGreen,
                    _root_ide_package_.com.example.presentation.screens.authentication.login.components.DeepGreen,
                    _root_ide_package_.com.example.presentation.screens.authentication.login.components.BaseGreen
                )
            ),
            size = size
        )

        /* ===============================
           ONDA ORGÂNICA (movimento)
           =============================== */
        val wavePath = Path().apply {
            val amplitude = height * 0.12f
            val centerY = height * (0.75f + waveOffset * 0.05f)

            moveTo(0f, centerY)

            cubicTo(
                width * 0.25f,
                centerY - amplitude,
                width * 0.75f,
                centerY + amplitude,
                width,
                centerY
            )

            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = wavePath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    _root_ide_package_.com.example.presentation.screens.authentication.login.components.AccentGreen.copy(alpha = 0.12f),
                    Color.Transparent
                )
            )
        )

        /* ===============================
           GLOW DE ENERGIA (radial)
           =============================== */
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    _root_ide_package_.com.example.presentation.screens.authentication.login.components.AccentGreen.copy(alpha = glowAlpha),
                    Color.Transparent
                ),
                center = Offset(
                    x = width / 2f,
                    y = height * 0.25f
                ),
                radius = width * 0.9f
            ),
            radius = width
        )
    }
}
