package com.example.presentation.ui.progress.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties

// ─────────────────────────────────────────────────────────────────────────────
// ProgressChartCard — card principal com toggle entre Linha e Colunas
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Card de gráfico com dois modos de visualização:
 * - **Linha** — tendência contínua das cargas ao longo das sessões.
 * - **Colunas** — comparativo de carga por sessão, lado a lado se houver dois períodos.
 *
 * Recebe `chartLines` já construídas pela ViewModel para que o card não
 * realize nenhum cálculo ou acesso a dados.
 *
 * @param chartLines Dados e estilo já preparados pela ViewModel.
 */
@Composable
fun ProgressChartCard(
    chartLines: List<Line>,
    modifier: Modifier = Modifier,
) {
    var activeTab by remember { mutableIntStateOf(0) }
    val hasData = chartLines.isNotEmpty() && chartLines.any { it.values.isNotEmpty() }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.04f)),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            ChartHeader(
                chartLines = chartLines,
                activeTab = activeTab,
                showToggle = hasData,
                onTabChange = { activeTab = it },
            )
            Spacer(Modifier.height(16.dp))

            AnimatedContent(
                targetState = if (!hasData) -1 else activeTab,
                transitionSpec = { fadeIn(tween(280)) togetherWith fadeOut(tween(200)) },
                label = "chart_type_transition",
            ) { tab ->
                when (tab) {
                    0    -> ProgressLineChart(chartLines)
                    1    -> ProgressColumnChart(chartLines)
                    else -> ChartEmptyState()
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Header com título, legenda e toggle Linha/Colunas
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ChartHeader(
    chartLines: List<Line>,
    activeTab: Int,
    showToggle: Boolean,
    onTabChange: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Progressão de Carga",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = "Peso levantado por sessão (kg)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.4f),
            )
            if (showToggle && chartLines.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    chartLines.forEach { line ->
                        val color = (line.color as? SolidColor)?.value ?: Color.White
                        LegendDot(label = line.label ?: "", color = color)
                    }
                }
            }
        }

        if (showToggle) {
            Spacer(Modifier.width(8.dp))
            ChartTypeToggle(activeTab = activeTab, onTabChange = onTabChange)
        }
    }
}

@Composable
private fun ChartTypeToggle(activeTab: Int, onTabChange: (Int) -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.06f),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.10f)),
    ) {
        Row(
            modifier = Modifier.padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            listOf(
                Icons.Outlined.ShowChart to "Linha",
                Icons.Outlined.BarChart  to "Colunas",
            ).forEachIndexed { idx, (icon, desc) ->
                val isSelected = activeTab == idx
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) Color.White.copy(alpha = 0.14f) else Color.Transparent,
                    onClick = { onTabChange(idx) },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = desc,
                            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.30f),
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendDot(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(modifier = Modifier.size(8.dp), shape = CircleShape, color = color) {}
        Spacer(Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.5f),
        )
    }
}

@Composable
private fun ChartEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Sem dados para o período selecionado",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.35f),
            textAlign = TextAlign.Center,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// LineChart — tendência de carga ao longo das sessões
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Gráfico de linha com estética OLED premium:
 * - Grid ultra-sutil (alpha 0.06) para não poluir visualmente.
 * - Curvas suaves sem pontos, stroke de 2.5 dp.
 * - Popup branco com valor ao tocar em qualquer ponto.
 * - Indicadores verticais em fonte leve lateral.
 *
 * Propriedades estáticas criadas com `remember` sem dependências —
 * recriadas apenas quando o componente entra na composição pela primeira vez.
 */
@Composable
private fun ProgressLineChart(chartLines: List<Line>) {
    val labelStyle = remember {
        TextStyle(
            color = Color.White.copy(alpha = 0.45f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
        )
    }

    val gridProperties = remember {
        GridProperties(
            xAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(Color.White.copy(alpha = 0.06f)),
                thickness = 0.5.dp,
            ),
            yAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(Color.White.copy(alpha = 0.06f)),
                thickness = 0.5.dp,
            ),
        )
    }

    val zeroLineProperties = remember {
        ZeroLineProperties(
            enabled = true,
            color = SolidColor(Color.White.copy(alpha = 0.15f)),
            thickness = 0.5.dp,
            style = StrokeStyle.Dashed(intervals = floatArrayOf(6f, 4f)),
        )
    }

    val indicatorProperties = remember {
        HorizontalIndicatorProperties(
            textStyle = TextStyle(
                color = Color.White.copy(alpha = 0.45f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            ),
            padding = 8.dp,
        )
    }

    val popupProperties = remember {
        PopupProperties(
            enabled = true,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            ),
            containerColor = Color.White,
            cornerRadius = 8.dp,
        )
    }

    val labelProperties = remember {
        LabelProperties(
            enabled = true,
            textStyle = labelStyle,
            padding = 12.dp,
        )
    }

    val labelHelperProperties = remember { LabelHelperProperties(enabled = false) }

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        data = chartLines,
        labelProperties = labelProperties,
        labelHelperProperties = labelHelperProperties,
        gridProperties = gridProperties,
        zeroLineProperties = zeroLineProperties,
        indicatorProperties = indicatorProperties,
        popupProperties = popupProperties,
        animationMode = AnimationMode.Together(delayBuilder = { it * 100L }),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// ColumnChart — carga por sessão como barras verticais
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Gráfico de colunas onde cada barra representa uma sessão de treino.
 *
 * Quando há dois períodos (atual + anterior), cada posição exibe duas barras
 * lado a lado com as cores de cada série — facilitando a comparação visual.
 *
 * A espessura e o espaçamento das barras se adaptam à quantidade de sessões
 * para evitar barras ou muito finas ou sobrepostas.
 */
@Composable
private fun ProgressColumnChart(chartLines: List<Line>) {
    val bars = remember(chartLines) { buildBarsFromLines(chartLines) }

    val barProperties = remember(bars.size) {
        BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
            spacing = when {
                bars.size <= 6  -> 6.dp
                bars.size <= 10 -> 4.dp
                else            -> 2.dp
            },
            thickness = when {
                bars.size <= 4  -> 28.dp
                bars.size <= 8  -> 18.dp
                bars.size <= 12 -> 12.dp
                else            -> 8.dp
            },
        )
    }

    val indicatorProperties = remember {
        HorizontalIndicatorProperties(
            textStyle = TextStyle(
                color = Color.White.copy(alpha = 0.45f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            ),
            padding = 8.dp,
        )
    }

    val gridProperties = remember {
        GridProperties(
            xAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(Color.White.copy(alpha = 0.06f)),
                thickness = 0.5.dp,
            ),
            yAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(Color.White.copy(alpha = 0.06f)),
                thickness = 0.5.dp,
            ),
        )
    }

    val labelProperties = remember {
        LabelProperties(
            enabled = true,
            textStyle = TextStyle(
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 10.sp,
            ),
            padding = 8.dp,
        )
    }

    val labelHelperProperties = remember { LabelHelperProperties(enabled = false) }

    val popupProperties = remember {
        PopupProperties(
            enabled = true,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            ),
            containerColor = Color.White,
            cornerRadius = 8.dp,
        )
    }

    val dividerProperties = remember {
        DividerProperties(
            xAxisProperties = LineProperties(
                color = SolidColor(Color.White.copy(alpha = 0.12f)),
                thickness = 0.5.dp,
            ),
            yAxisProperties = LineProperties(
                color = SolidColor(Color.White.copy(alpha = 0.12f)),
                thickness = 0.5.dp,
            ),
        )
    }

    ColumnChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        data = bars,
        barProperties = barProperties,
        indicatorProperties = indicatorProperties,
        gridProperties = gridProperties,
        labelProperties = labelProperties,
        labelHelperProperties = labelHelperProperties,
        popupProperties = popupProperties,
        dividerProperties = dividerProperties,
        animationMode = AnimationMode.Together(delayBuilder = { it * 50L }),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Conversão Line → Bars
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Converte a lista de [Line] do gráfico de linha para [Bars] do gráfico de colunas.
 *
 * Cada índice da lista de valores vira um grupo de barras (uma por série/linha),
 * mantendo a mesma cor e rótulo de cada série.
 */
private fun buildBarsFromLines(lines: List<Line>): List<Bars> {
    if (lines.isEmpty()) return emptyList()
    val maxSessions = lines.maxOf { it.values.size }
    if (maxSessions == 0) return emptyList()

    return (0 until maxSessions).map { sessionIdx ->
        Bars(
            label = "S${sessionIdx + 1}",
            values = lines.mapNotNull { line ->
                if (sessionIdx < line.values.size) {
                    Bars.Data(
                        label = line.label,
                        value = line.values[sessionIdx],
                        color = (line.color as? SolidColor) ?: SolidColor(Color.White),
                    )
                } else null
            },
        )
    }
}
