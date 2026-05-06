// UltraFitnessOnboardingBackground.kt
package com.example.presentation.ui.onboarding.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
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
    colors: ColorScheme, // Injetando o ColorScheme Neon Circuit
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "onboarding_anim")

    val time by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(26_000, easing = LinearEasing)
        ), label = "time_float"
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

        // Fundo principal: Gradiente do Deep Neutral (#0A0B0F) para Surface (#16171D)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    colors.background,
                    colors.surface,
                    colors.surfaceVariant
                )
            )
        )

        // Ondas sutis — usando o PrimaryContainer (seu Roxo de profundidade #2D1B59)
        // Mantemos a opacidade baixíssima (0.03f) para um efeito de "fumaça neon"
        drawWave(
            time = time,
            amplitude = 26f,
            color = colors.primaryContainer.copy(alpha = 0.035f),
            heightFactor = 0.30f
        )

        drawWave(
            time = time * 1.35f,
            amplitude = 40f,
            color = colors.primaryContainer.copy(alpha = 0.028f),
            heightFactor = 0.52f
        )

        // Partículas discretas — Usando o Secondary (Azul Elétrico) para brilhos sutis
        onboardingParticles.forEach { p ->
            val progress = ((time + p.phase) * p.speed) % 360f / 360f
            val y = (p.y - progress).mod(1f) * h
            val x = (p.x + sin((time + p.phase).toRad()) * p.drift * 0.02f).mod(1f) * w

            drawCircle(
                color = colors.secondary.copy(alpha = p.alpha * 0.6f),
                radius = p.radius,
                center = Offset(x, y)
            )
        }

        // Glow central — PrimaryContainer para criar o foco atrás da ilustração do onboarding
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    colors.primaryContainer.copy(alpha = 0.15f),
                    Color.Transparent
                )
            ),
            radius = w * 0.55f,
            center = Offset(w / 2f, h * 0.40f)
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