// UltraFitnessOnboardingBackground.kt
package com.example.presentation.screens.ui.onboarding.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.presentation.theme.DarkGamifiedColors
import kotlin.random.Random
import kotlin.math.PI
import kotlin.math.sin

private data class OnboardingParticle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val speed: Float,
    val alpha: Float,
    val drift: Float,
    val phase: Float
)

@Composable
fun UltraFitnessOnboardingBackground(
    step: Int,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition()

    val time by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(26_000, easing = LinearEasing)
        )
    )

    val onboardingParticles = remember {
        List(28) {
            OnboardingParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 3.5f + 1.5f,
                speed = Random.nextFloat() * 0.18f + 0.04f,
                alpha = Random.nextFloat() * 0.18f + 0.03f,
                drift = Random.nextFloat() * 0.5f - 0.25f,
                phase = Random.nextFloat() * 360f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Fundo principal: gradiente escuro usando Background -> Surface -> Card
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    DarkGamifiedColors.Background,
                    DarkGamifiedColors.Surface,
                    DarkGamifiedColors.Card
                ),
                startY = 0f,
                endY = h
            )
        )

        // Ondas sutis — usando PrimarySoft numa opacidade bem baixa para não brilhar demais
        drawWave(
            time = time,
            amplitude = 26f,
            color = DarkGamifiedColors.PrimarySoft.copy(alpha = 0.035f),
            heightFactor = 0.30f
        )

        drawWave(
            time = time * 1.35f,
            amplitude = 40f,
            color = DarkGamifiedColors.PrimarySoft.copy(alpha = 0.028f),
            heightFactor = 0.52f
        )

        drawWave(
            time = time * 1.75f,
            amplitude = 54f,
            color = DarkGamifiedColors.PrimarySoft.copy(alpha = 0.022f),
            heightFactor = 0.70f
        )

        // Partículas discretas — ainda usando Accent, porém muito sutis
        onboardingParticles.forEach { p ->
            val progress = ((time + p.phase) * p.speed) % 360f / 360f
            val y = (p.y - progress).mod(1f) * h
            val x = (
                    p.x +
                            sin((time + p.phase).toRad()) * p.drift * 0.02f
                    ).mod(1f) * w

            drawCircle(
                color = DarkGamifiedColors.Accent.copy(alpha = p.alpha * 0.9f),
                radius = p.radius,
                center = Offset(x, y)
            )
        }

        // Glow central sutil usando PrimarySoft (escuro) para não competir com conteúdo
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DarkGamifiedColors.PrimarySoft.copy(alpha = 0.10f),
                    Color.Transparent
                )
            ),
            radius = w * 0.48f,
            center = Offset(w / 2f, h * 0.36f)
        )
    }
}

/** Wave drawer (path) — amostragem otimizada para performance */
private fun DrawScope.drawWave(
    time: Float,
    amplitude: Float,
    color: Color,
    heightFactor: Float
) {
    val path = Path()
    val width = size.width
    val height = size.height

    path.moveTo(0f, height * heightFactor)

    val stepPx = 14 // passo maior = menos pontos = melhor performance em telas grandes
    var x = 0
    while (x <= width.toInt()) {
        val norm = x.toFloat() / width.toFloat() // 0..1
        val y = height * heightFactor +
                (sin(norm * 2f * PI + time.toRad()) * amplitude).toFloat()
        path.lineTo(x.toFloat(), y)
        x += stepPx
    }

    path.lineTo(width, height)
    path.lineTo(0f, height)
    path.close()

    drawPath(path = path, color = color)
}

/** Helper: graus -> radianos */
fun Float.toRad(): Float = this * (PI.toFloat() / 180f)