package com.example.presentation.screens.onboarding.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.lerp
import com.example.domain.model.onboarding.OnboardingParticle
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.BaseGreen
import com.example.presentation.theme.DeepGreen
import kotlin.math.sin
import kotlin.random.Random
import kotlin.math.PI

@Composable
fun UltraFitnessOnboardingBackground(step: Int) {

    val infinite = rememberInfiniteTransition()

    val time by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(26000, easing = LinearEasing)
        )
    )

    val onboardingParticles = remember {
        List(32) {
            OnboardingParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 4f + 2f,
                speed = Random.nextFloat() * 0.2f + 0.05f,
                alpha = Random.nextFloat() * 0.25f + 0.05f,
                drift = Random.nextFloat() * 0.5f - 0.25f,
                phase = Random.nextFloat() * 360f
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {

        val w = size.width
        val h = size.height

        // 🎨 Fundo principal (login-like)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    lerp(BaseGreen, DeepGreen, step / 3f),
                    lerp(DeepGreen, BaseGreen, step / 3f)
                )
            )
        )

        // 🌊 Ondas de profundidade
        drawWave(
            time = time,
            amplitude = 34f,
            color = AccentGreen.copy(alpha = 0.06f),
            heightFactor = 0.32f
        )

        drawWave(
            time = time * 1.4f,
            amplitude = 48f,
            color = AccentGreen.copy(alpha = 0.06f),
            heightFactor = 0.50f
        )

        drawWave(
            time = time * 1.8f,
            amplitude = 60f,
            color = AccentGreen.copy(alpha = 0.06f),
            heightFactor = 0.65f
        )

        // ✨ Partículas energéticas (discretas)
        onboardingParticles.forEach { p ->

            val progress =
                ((time + p.phase) * p.speed) % 360f / 360f

            val y = (p.y - progress).mod(1f) * h
            val x = (
                    p.x +
                            _root_ide_package_.kotlin.math.sin((time + p.phase).toRad()) * p.drift * 0.02f
                    ).mod(1f) * w

            drawCircle(
                color = AccentGreen.copy(alpha = p.alpha),
                radius = p.radius,
                center = Offset(x, y)
            )
        }

        // 🔆 Glow sutil central
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AccentGreen.copy(alpha = 0.18f),
                    Color.Transparent
                )
            ),
            radius = w * 0.55f,
            center = Offset(w / 2f, h * 0.35f)
        )
    }
}


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

    for (x in 0..width.toInt() step 12) {
        val y = height * heightFactor +
                sin((x / width) * 2f * PI + time.toRad()).toFloat() * amplitude
        path.lineTo(x.toFloat(), y)
    }

    path.lineTo(width, height)
    path.lineTo(0f, height)
    path.close()

    drawPath(path, color)
}

private fun Float.toRad(): Float =
    this * PI.toFloat() / 180f
