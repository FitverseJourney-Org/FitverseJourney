package com.example.presentation.screens.ui.progress.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.progress.InsightLevel
import com.example.domain.model.progress.ProgressionInsight
import com.example.domain.model.progress.ProgressionStats
import com.example.presentation.utils.formatDecimalKmp

// ─────────────────────────────────────────────────────────────────────────────
// ProgressionStatsGrid — grade 2×2 de cards de estatística
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Grid 2×2 com as quatro métricas principais da sessão.
 *
 * Recebe o [ProgressionStats] já calculado pela ViewModel — sem lógica
 * condicional aqui. A UI apenas mapeia dados → elementos visuais.
 */
@Composable
fun ProgressionStatsGrid(
    stats: ProgressionStats,
    accentColor: Color = Color.White,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Recorde pessoal — destaque visual máximo
            GlassStatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.AutoGraph,
                label = "Recorde",
                value = stats.personalRecord.formatDecimalKmp(),
                unit = "kg",
                accentColor = accentColor,
                isHighlighted = true,
            )
            GlassStatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.AutoGraph,
                label = "Carga Atual",
                value = stats.currentLoad.formatDecimalKmp(),
                unit = "kg",
                accentColor = Color.White.copy(alpha = 0.7f),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val isPositive = stats.evolutionDelta >= 0
            val deltaColor = if (isPositive) Color(0xFF66BB6A) else Color(0xFFEF5350)
            val sign = if (isPositive) "+" else ""

            GlassStatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.AutoGraph,
                label = "Evolução",
                value = "$sign${stats.evolutionDelta.formatDecimalKmp()}",
                unit = "kg",
                accentColor = deltaColor,
            )
            GlassStatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.AutoGraph,
                label = "Treinos",
                value = stats.sessionCount.toString(),
                unit = "sessões",
                accentColor = Color.White.copy(alpha = 0.45f),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// GlassStatCard — design minimalista com toque glassmorphic
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Card de estatística com estética "glass dark":
 * - Fundo semi-transparente com borda sutil.
 * - Ícone em pill colorido para identificação rápida.
 * - Animação de contagem nos valores numéricos.
 *
 * @param isHighlighted Aplica borda de destaque adicional (para o PR).
 */
@Composable
fun GlassStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    unit: String,
    accentColor: Color,
    isHighlighted: Boolean = false,
) {
    val borderColor = if (isHighlighted) accentColor.copy(alpha = 0.35f)
    else Color.White.copy(alpha = 0.07f)
    val borderWidth = if (isHighlighted) 1.dp else 0.5.dp

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.04f),
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(borderWidth, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Ícone pill
            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.12f),
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(20.dp),
                )
            }

            // Valor + unidade
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor,
                    letterSpacing = (-0.5).sp,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.45f),
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            // Label
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.35f),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

/**
 * Variante de [GlassStatCard] com animação de contagem para valores Double.
 * Use para métricas que mudam frequentemente (carga, PR, evolução).
 */
@Composable
fun AnimatedGlassStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    rawValue: Double,
    unit: String,
    accentColor: Color,
    isHighlighted: Boolean = false,
) {
    val animatedValue by animateFloatAsState(
        targetValue = rawValue.toFloat(),
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "stat_anim_$label",
    )
    GlassStatCard(
        modifier = modifier,
        icon = icon,
        label = label,
        value = animatedValue.toDouble().formatDecimalKmp(),
        unit = unit,
        accentColor = accentColor,
        isHighlighted = isHighlighted,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// InsightsCard refatorado
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Card de insight com cor adaptada ao [InsightLevel].
 *
 * Não contém lógica de cálculo — recebe o [ProgressInsight] pronto
 * da ViewModel e apenas mapeia o nível para uma cor de destaque.
 */
@Composable
fun InsightsCard(
    insight: ProgressionInsight,
    modifier: Modifier = Modifier,
) {
    val levelColor = when (insight.level) {
        InsightLevel.EXCELLENT -> Color(0xFF66BB6A)
        InsightLevel.GOOD      -> Color(0xFF42A5F5)
        InsightLevel.NEUTRAL   -> Color(0xFFFFCA28)
        InsightLevel.NO_DATA   -> Color.White.copy(alpha = 0.35f)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = levelColor.copy(alpha = 0.06f),
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(0.5.dp, levelColor.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = CircleShape,
                color = levelColor.copy(alpha = 0.15f),
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = levelColor,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(20.dp),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Insight",
                    style = MaterialTheme.typography.labelSmall,
                    color = levelColor,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = insight.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.75f),
                    lineHeight = 20.sp,
                )
            }
        }
    }
}