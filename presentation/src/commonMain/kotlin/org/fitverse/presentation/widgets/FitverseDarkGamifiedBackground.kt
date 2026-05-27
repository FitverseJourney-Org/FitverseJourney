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

/**
 * Background gamificado OLED.
 *
 * @param accentColor cor dominante do tema — padrão usa [MaterialTheme.colorScheme.primary].
 *   Passe a cor do tipo aeróbio ([ActivityType.color]) para tematizar o fundo por modalidade.
 */
@Composable
fun DarkGamifiedDashboardBackground(
    modifier: Modifier = Modifier,
    accentColor: Color = Color.Unspecified,
) {
    val colors  = MaterialTheme.colorScheme
    val primary = if (accentColor == Color.Unspecified) colors.primary else accentColor

    val infinite = rememberInfiniteTransition(label = "TriadTransition")

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

        // 0. Fundo OLED absoluto
        drawRect(color = colors.background)

        // 1. TOPO: Aura da modalidade — pulsa lentamente
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(primary.copy(alpha = 0.13f * topPulse), Color.Transparent),
                center = Offset(w * 0.5f, h * 0.05f),
                radius = w * 0.85f
            ),
            radius = w * 0.85f,
            center = Offset(w * 0.5f, h * 0.05f)
        )

        // 2. MEIO: Vórtice dinâmico — deriva lateralmente
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(primary.copy(alpha = 0.07f), Color.Transparent),
                center = Offset(w / 2f + midShift, h * 0.45f),
                radius = w * 0.72f
            ),
            radius = w * 0.72f,
            center = Offset(w / 2f + midShift, h * 0.45f),
            blendMode = BlendMode.Screen
        )

        // 3. FOOTER: Névoa de suporte para a NavigationBar
        val footerPath = Path().apply {
            val footerY = h * 0.88f
            moveTo(0f, footerY)
            cubicTo(w * 0.3f, footerY - 30f, w * 0.7f, footerY + 30f, w, footerY)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            path = footerPath,
            brush = Brush.verticalGradient(
                colors = listOf(primary.copy(alpha = 0.12f), colors.background)
            )
        )

        // 4. FLARE: micro-glow de acabamento no canto superior direito
        drawCircle(
            color  = primary.copy(alpha = 0.04f),
            radius = w * 0.4f,
            center = Offset(w * 0.9f, h * 0.22f)
        )
    }
}