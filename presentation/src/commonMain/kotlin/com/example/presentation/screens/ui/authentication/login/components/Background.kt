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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.domain.model.onboarding.OnboardingParticle
import com.example.presentation.theme.DarkGamifiedColors
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun AnimatedLoginBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Movimento de "respiração" da onda principal
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Pulsação do Glow de Acento (Accent)
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(6_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Reutilizando a lógica de partículas para dar vida ao fundo
    val particles = remember {
        List(15) { // Menos partículas que o onboarding para não distrair no Login
            OnboardingParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 2f + 1f,
                speed = Random.nextFloat() * 0.1f + 0.02f,
                alpha = Random.nextFloat() * 0.15f + 0.05f,
                drift = Random.nextFloat() * 0.4f - 0.2f,
                phase = Random.nextFloat() * 360f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. FUNDO: Gradiente profundo (Identidade Fitverse)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    DarkGamifiedColors.Background,
                    DarkGamifiedColors.Surface,
                ),
                startY = 0f,
                endY = h
            )
        )

        // 2. PARTÍCULAS: Movimento sutil em Background
        particles.forEach { p ->
            // Simulação de subida infinita baseada no tempo (usando o waveOffset como driver de tempo)
            val y = (p.y - (waveOffset * p.speed)).mod(1f) * h
            val x = (p.x * w) + (sin(waveOffset * 2 * PI.toFloat()) * 10f)

            drawCircle(
                color = DarkGamifiedColors.Primary.copy(alpha = p.alpha),
                radius = p.radius,
                center = Offset(x, y)
            )
        }

        // 3. ONDA ORGÂNICA: Usando PrimarySoft para um "mood" de tecnologia
        val wavePath = Path().apply {
            val amplitude = h * 0.08f
            val centerY = h * (0.70f + waveOffset * 0.05f)

            moveTo(0f, centerY)
            // Curva Bézier para um aspecto mais moderno/líquido que o Seno purp
            cubicTo(
                w * 0.25f, centerY - amplitude,
                w * 0.75f, centerY + amplitude,
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
                    DarkGamifiedColors.PrimarySoft.copy(alpha = 0.12f),
                    DarkGamifiedColors.PrimarySoft.copy(alpha = 0.02f),
                    Color.Transparent
                )
            )
        )

        // 4. GLOW CENTRAL (Foco no Conteúdo): Accent Color
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DarkGamifiedColors.Primary.copy(alpha = glowAlpha),
                    Color.Transparent
                )
            ),
            radius = w * 0.8f,
            center = Offset(w / 2f, h * 0.30f)
        )

        // 5. GLOW DE PROFUNDIDADE: PrimarySoft na base
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DarkGamifiedColors.PrimarySoft.copy(alpha = 0.05f),
                    Color.Transparent
                )
            ),
            radius = w * 0.5f,
            center = Offset(w * 0.1f, h * 0.85f)
        )
    }
}