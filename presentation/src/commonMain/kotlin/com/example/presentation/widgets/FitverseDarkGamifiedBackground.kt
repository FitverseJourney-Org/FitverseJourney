package org.fitverse.presentation.widgets

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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun DarkGamifiedDashboardBackground(
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val infinite = rememberInfiniteTransition(label = "TriadTransition")

    // Animações independentes para cada zona (Paralaxe de brilho)
    val topPulse by infinite.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(12000), RepeatMode.Reverse), label = "top"
    )
    val midShift by infinite.animateFloat(
        initialValue = -50f, targetValue = 50f,
        animationSpec = infiniteRepeatable(tween(15000), RepeatMode.Reverse), label = "mid"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 0. Fundo OLED Absoluto
        drawRect(color = colors.background)

        // 1. TOPO: Aura de Identidade (Roxo Primary)
        // Foca no status do Avatar e XP
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(colors.primary.copy(alpha = 0.12f * topPulse), Color.Transparent),
                center = Offset(w * 0.5f, h * 0.05f),
                radius = w * 0.8f
            ),
            radius = w * 0.8f,
            center = Offset(w * 0.5f, h * 0.05f)
        )

        // 2. MEIO: Vortex de Atividade (Azul Secondary)
        // Cria profundidade atrás dos widgets principais
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(colors.secondary.copy(alpha = 0.06f), Color.Transparent),
                center = Offset(w / 2f + midShift, h * 0.45f),
                radius = w * 0.7f
            ),
            radius = w * 0.7f,
            center = Offset(w / 2f + midShift, h * 0.45f),
            blendMode = BlendMode.Screen
        )

        // 3. BOTTOM: Névoa de Estabilidade (PrimaryContainer/Deep Purple)
        // Dá suporte visual para a NavigationBar transparente
        val footerPath = Path().apply {
            val footerY = h * 0.88f
            moveTo(0f, footerY)
            cubicTo(
                w * 0.3f, footerY - 30f,
                w * 0.7f, footerY + 30f,
                w, footerY
            )
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            path = footerPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    colors.primaryContainer.copy(alpha = 0.15f),
                    colors.background
                )
            )
        )

        // 4. DETALHE SÊNIOR: Micro-Glow de Acabamento (Verde Tertiary)
        // Um pequeno "flare" de saúde que aparece e desaparece no canto
        drawCircle(
            color = colors.tertiary.copy(alpha = 0.03f),
            radius = w * 0.4f,
            center = Offset(w * 0.9f, h * 0.25f)
        )
    }
}