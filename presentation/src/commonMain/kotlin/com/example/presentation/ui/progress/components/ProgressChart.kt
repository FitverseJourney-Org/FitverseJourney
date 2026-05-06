package com.example.presentation.ui.progress.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties

/**
 * Card do gráfico de progressão de carga.
 *
 * ## Otimizações de performance
 *
 * - Recebe `chartLines: List<Line>` já construída pela ViewModel.
 *   O `remember` no `data =` parâmetro do `LineChart` é desnecessário aqui
 *   porque a referência à lista só muda quando a ViewModel emite um novo estado —
 *   ou seja, apenas quando os dados realmente mudaram.
 *
 * - As propriedades estáticas do gráfico (GridProperties, LabelProperties etc.)
 *   são declaradas com `remember(Unit)` sem dependências, garantindo que os
 *   objetos sejam criados uma única vez por composição do card e nunca
 *   recriados em recomposições subsequentes causadas por outros fatores.
 *
 * - O componente é responsável apenas pelo gráfico. Filtros e estatísticas
 *   ficam em composables irmãos, evitando que uma mudança de filtro
 *   force recomposição do gráfico quando os dados são os mesmos.
 *
 * @param chartLines Lista de [Line] montada pela ViewModel com dados e cores prontos.
 */
@Composable
fun ProgressChartCard(
    chartLines: List<Line>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.04f),
        ),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Cabeçalho
            ChartHeader(chartLines)
            Spacer(Modifier.height(20.dp))

            // Corpo: gráfico ou empty state
            if (chartLines.isEmpty() || chartLines.all { it.values.isEmpty() }) {
                ChartEmptyState()
            } else {
                ProgressLineChart(chartLines)
            }
        }
    }
}

@Composable
private fun ChartHeader(lines: List<Line>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
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
        }
        // Legenda inline das séries
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            lines.forEach { line ->
                val lineColor = (line.color as? SolidColor)?.value ?: Color.White
                LegendDot(label = line.label ?: "", color = lineColor)
            }
        }
    }
}

@Composable
private fun LegendDot(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(8.dp),
            shape = CircleShape,
            color = color,
        ) {}
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

/**
 * LineChart configurado para estética "OLED premium":
 * - Grid quase invisível (alpha 0.06) para não poluir o visual.
 * - Sem pontos nas linhas — curvas limpas.
 * - Popup de valor habilitado para interatividade.
 * - Indicadores verticais com fonte leve.
 *
 * As propriedades são criadas com `remember` sem dependências —
 * nunca mudam e não precisam ser recriadas a cada recomposição.
 */
@Composable
private fun ProgressLineChart(chartLines: List<Line>) {
    val labelTextStyle = remember {
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
        )
    }

    val indicatorProperties = remember {
        HorizontalIndicatorProperties(
            textStyle = TextStyle(
                color = Color.White.copy(alpha = 0.45f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            )
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
        )
    }

    val labelProperties = remember {
        LabelProperties(
            enabled = true,
            textStyle = labelTextStyle,
            padding = 12.dp,
        )
    }

    val labelHelperProperties = remember {
        LabelHelperProperties(
            enabled = false, // Legenda feita manualmente no header
        )
    }

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
    )
}