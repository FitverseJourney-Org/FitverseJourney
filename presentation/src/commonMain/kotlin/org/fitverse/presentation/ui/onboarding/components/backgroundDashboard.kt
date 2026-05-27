package org.fitverse.presentation.ui.onboarding.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
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
import kotlin.math.cos
import kotlin.math.sin

private data class PremiumParticle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val speed: Float,
    val alpha: Float,
    val drift: Float,
    val phase: Float
)

@Composable
fun PremiumGamifiedBackground(
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val infinite = rememberInfiniteTransition()

    val time by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(32_000, easing = LinearEasing)
        )
    )

    val particles = remember {
        List(24) {
            PremiumParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 2.5f + 1f,
                speed = Random.nextFloat() * 0.12f + 0.03f,
                alpha = Random.nextFloat() * 0.15f + 0.04f,
                drift = Random.nextFloat() * 0.4f - 0.2f,
                phase = Random.nextFloat() * 360f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {

        val w = size.width
        val h = size.height

        // 🔹 Fundo base diagonal (diferente do vertical original)
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    cs.background,
                    cs.surface,
                    cs.surfaceVariant
                ),
                start = Offset(0f, 0f),
                end = Offset(w, h)
            )
        )

        // 🔹 Neblina orgânica (não é wave clássica)
        drawNebula(
            time = time,
            amplitude = 38f,
            color = cs.primary.copy(alpha = 0.035f),
            verticalBias = 0.35f
        )

        drawNebula(
            time = time * 1.4f,
            amplitude = 52f,
            color = cs.secondary.copy(alpha = 0.025f),
            verticalBias = 0.65f
        )

        // 🔹 Partículas mais discretas
        particles.forEach { p ->
            val progress = ((time + p.phase) * p.speed) % 360f / 360f

            val y = (p.y - progress).mod(1f) * h
            val x = (
                    p.x + sin((time + p.phase).toRad()) * p.drift * 0.02f
                    ).mod(1f) * w

            drawCircle(
                color = cs.secondary.copy(alpha = p.alpha),
                radius = p.radius,
                center = Offset(x, y)
            )
        }

        // 🔹 Halo deslocado (não central)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    cs.primaryContainer.copy(alpha = 0.12f),
                    Color.Transparent
                )
            ),
            radius = w * 0.55f,
            center = Offset(w * 0.75f, h * 0.25f)
        )
    }
}

private fun DrawScope.drawNebula(
    time: Float,
    amplitude: Float,
    color: Color,
    verticalBias: Float
) {
    val path = Path()
    val width = size.width
    val height = size.height

    path.moveTo(0f, height * verticalBias)

    val stepPx = 18
    var x = 0

    while (x <= width.toInt()) {
        val norm = x.toFloat() / width
        val organic =
            sin(norm * 3f * PI + time.toRad()) * 0.6f +
                    cos(norm * 5f * PI + time.toRad() * 1.2f) * 0.4f

        val y = height * verticalBias + (organic * amplitude).toFloat()

        path.lineTo(x.toFloat(), y)
        x += stepPx
    }

    path.lineTo(width, height)
    path.lineTo(0f, height)
    path.close()

    drawPath(path = path, color = color)
}