package com.example.presentation.components.background

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


@Composable
fun ModernFitverseBackground(
    modifier: Modifier = Modifier,
) {
    // Consome as cores que você já definiu no seu FitverseTheme
    val cs = MaterialTheme.colorScheme

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Fundo Base (Sólido, limpo e escuro)
        drawRect(color = cs.background)

        // 2. Glow Radial no Topo (Amarelo Neon muito sutil)
        // Cria uma leve iluminação vindo de cima, destacando a TopBar/Header
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    cs.onSurface.copy(alpha = 0.05f), // Apenas 5% de opacidade
                    Color.Transparent
                ),
                center = Offset(x = w * 0.5f, y = -h * 0.1f), // Centro superior
                radius = w * 1.2f
            )
        )

        // 3. Glow Radial Inferior Direito (Roxo Secundário)
        // Traz um balanço térmico (quente/frio) para a tela sem roubar a cena
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    cs.secondary.copy(alpha = 0.04f), // Apenas 4% de opacidade
                    Color.Transparent
                )
            ),
            radius = w * 0.8f,
            center = Offset(x = w, y = h)
        )
    }
}