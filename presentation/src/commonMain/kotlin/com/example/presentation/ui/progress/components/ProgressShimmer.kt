package com.example.presentation.ui.progress.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// Shimmer Brush reutilizável
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Gera um [Brush] com gradiente animado para o efeito shimmer.
 *
 * Centralizado aqui para que todos os skeletons tenham timing idêntico,
 * criando a ilusão de uma "onda de luz" coerente na tela inteira.
 */
@Composable
fun rememberShimmerBrush(
    baseColor: Color = Color.White.copy(alpha = 0.04f),
    highlightColor: Color = Color.White.copy(alpha = 0.12f),
): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer_translate",
    )
    return Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(translateAnim - 300f, 0f),
        end = Offset(translateAnim, 0f),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Bloco de esqueleto genérico
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ShimmerBlock(
    modifier: Modifier = Modifier,
    brush: Brush = rememberShimmerBrush(),
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(brush)
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Skeleton completo da ProgressScreen
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Layout esqueleto que espelha a estrutura real da ProgressScreen.
 *
 * Usar um skeleton que imita o layout final (em vez de um spinner genérico)
 * reduz o "salto visual" quando o conteúdo real aparece, melhorando
 * a percepção de performance pelo usuário.
 */
@Composable
fun ProgressScreenSkeleton(modifier: Modifier = Modifier) {
    val brush = rememberShimmerBrush()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Skeleton das tabs de ficha
        TabsSkeleton(brush)

        // Skeleton do seletor de exercício
        ExerciseSelectorSkeleton(brush)

        // Skeleton do filtro de período
        PeriodFilterSkeleton(brush)

        // Skeleton do gráfico
        ChartSkeleton(brush)

        // Skeleton dos stat cards (grid 2x2)
        StatGridSkeleton(brush)

        // Skeleton do insight
        InsightSkeleton(brush)
    }
}

@Composable
private fun TabsSkeleton(brush: Brush) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(4) { index ->
            ShimmerBlock(
                brush = brush,
                modifier = Modifier
                    .weight(if (index == 0) 1.4f else 1f)
                    .height(36.dp),
            )
        }
    }
}

@Composable
private fun ExerciseSelectorSkeleton(brush: Brush) {
    ShimmerBlock(
        brush = brush,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
    )
}

@Composable
private fun PeriodFilterSkeleton(brush: Brush) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(2) {
            ShimmerBlock(
                brush = brush,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
            )
        }
    }
}

@Composable
private fun ChartSkeleton(brush: Brush) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Título do card
            ShimmerBlock(
                brush = brush,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(16.dp),
            )
            Spacer(Modifier.height(20.dp))
            // Área do gráfico
            ShimmerBlock(
                brush = brush,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }
    }
}

@Composable
private fun StatGridSkeleton(brush: Brush) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                repeat(2) {
                    ShimmerBlock(
                        brush = brush,
                        modifier = Modifier
                            .weight(1f)
                            .height(88.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightSkeleton(brush: Brush) {
    ShimmerBlock(
        brush = brush,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
    )
}