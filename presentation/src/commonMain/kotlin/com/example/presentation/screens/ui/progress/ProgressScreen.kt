package com.example.presentation.screens.ui.progress

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.PlatformDate
import com.example.expect.formatDecimal
import com.example.presentation.screens.widgets.FitverseTopAppBar
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties

private fun getMonthName(month: Int): String {
    val months = arrayOf(
        "", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    )
    return months.getOrElse(month) { month.toString() }
}

@Immutable
data class LoadProgressionPoint(
    val date: PlatformDate,
    val weight: Double,
    val estimatedOneRm: Double
)

data class ProgressionStats(
    val personalRecord: Double,
    val currentLoad: Double,
    val evolutionDelta: Double,
    val sessionCount: Int
)

fun calculateProgressionStats(data: List<LoadProgressionPoint>): ProgressionStats {
    if (data.isEmpty()) return ProgressionStats(0.0, 0.0, 0.0, 0)

    val pr = data.maxOfOrNull { it.weight } ?: 0.0
    val current = data.last().weight
    val first = data.first().weight
    val delta = current - first

    return ProgressionStats(
        personalRecord = pr,
        currentLoad = current,
        evolutionDelta = delta,
        sessionCount = data.size
    )
}

@Composable
fun ProgressionStatsGrid(
    progressionData: List<LoadProgressionPoint>,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    if (progressionData.isEmpty()) return

    val stats = remember(progressionData) { calculateProgressionStats(progressionData) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Recorde Pessoal",
                value = stats.personalRecord,
                unit = "kg",
                icon = Icons.Default.EmojiEvents,
                color = primaryColor
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Carga Atual",
                value = stats.currentLoad,
                unit = "kg",
                icon = Icons.Default.FitnessCenter,
                color = Color.White
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            val isPositive = stats.evolutionDelta >= 0
            val sign = if (isPositive) "+" else ""
            val deltaColor = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)

            StatCardString(
                modifier = Modifier.weight(1f),
                title = "Evolução",
                value = "$sign${stats.evolutionDelta.formatDecimal()}",
                unit = "kg",
                icon = Icons.Default.TrendingUp,
                color = deltaColor
            )
            StatCardString(
                modifier = Modifier.weight(1f),
                title = "Treinos",
                value = stats.sessionCount.toString(),
                unit = "sessões",
                icon = Icons.Default.CalendarToday,
                color = Color.LightGray
            )
        }
    }
}

@Immutable
data class Exercise(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val trainingSplit: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    exercises: List<Exercise>,
    currentProgression: List<LoadProgressionPoint>,
    monthbeforeProgression: List<LoadProgressionPoint>,
    onBack: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Filtros de Exercício
    val availableSplits = remember(exercises) { exercises.map { it.trainingSplit }.distinct().sorted() }
    var selectedSplit by remember { mutableStateOf(availableSplits.firstOrNull() ?: "") }
    var splitDropdownOpen by remember { mutableStateOf(false) }

    val filteredExercises = remember(selectedSplit, exercises) { exercises.filter { it.trainingSplit == selectedSplit } }
    var selectedExercise by remember(filteredExercises) { mutableStateOf(filteredExercises.firstOrNull()) }
    var exerciseDropdownOpen by remember { mutableStateOf(false) }

    // Filtros de Período
    val availableMonths = remember { (1..12).toList() }
    var startMonth by remember { mutableIntStateOf(1) }
    var endMonth by remember { mutableIntStateOf(12) }
    var startDropdownOpen by remember { mutableStateOf(false) }
    var endDropdownOpen by remember { mutableStateOf(false) }

    // Dados Filtrados
    val displayData = remember(startMonth, endMonth, currentProgression) {
        currentProgression.filter { it.date.month in startMonth..endMonth }
    }
    val displayDataBefore = remember(startMonth, endMonth, monthbeforeProgression) {
        monthbeforeProgression.filter { it.date.month in startMonth..endMonth }
    }

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    Scaffold(
        topBar = { FitverseTopAppBar(title = "PROGRESSÃO", onBack = onBack) },
        containerColor = cs.background
    ) { padding ->
        AnimatedVisibility(visible = isVisible, modifier = Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Filtros (Cabeçalho)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(.5f)) {
                                SelectorCard(
                                    label = "Ficha",
                                    value = selectedSplit,
                                    icon = Icons.Default.ListAlt,
                                    onClick = { splitDropdownOpen = true }
                                )
                                DropdownMenu(
                                    expanded = splitDropdownOpen,
                                    onDismissRequest = { splitDropdownOpen = false },
                                    modifier = Modifier.width(150.dp).heightIn(max = 250.dp)
                                ) {
                                    availableSplits.forEach { split ->
                                        DropdownMenuItem(
                                            text = { Text(split) },
                                            onClick = { selectedSplit = split; splitDropdownOpen = false }
                                        )
                                    }
                                }
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                SelectorCard(
                                    label = "Exercício",
                                    value = selectedExercise?.name ?: "Selecione",
                                    icon = Icons.Default.FitnessCenter,
                                    onClick = { exerciseDropdownOpen = true }
                                )
                                DropdownMenu(
                                    expanded = exerciseDropdownOpen,
                                    onDismissRequest = { exerciseDropdownOpen = false },
                                    modifier = Modifier.width(220.dp).heightIn(max = 250.dp)
                                ) {
                                    filteredExercises.forEach { ex ->
                                        DropdownMenuItem(
                                            text = { Text(ex.name) },
                                            onClick = { selectedExercise = ex; exerciseDropdownOpen = false }
                                        )
                                    }
                                }
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                SelectorCard(
                                    label = "Início",
                                    value = getMonthName(startMonth),
                                    icon = Icons.Default.CalendarMonth,
                                    onClick = { startDropdownOpen = true }
                                )
                                DropdownMenu(
                                    expanded = startDropdownOpen,
                                    onDismissRequest = { startDropdownOpen = false },
                                    modifier = Modifier.width(180.dp).heightIn(max = 280.dp)
                                ) {
                                    availableMonths.forEach { month ->
                                        DropdownMenuItem(
                                            text = { Text(getMonthName(month)) },
                                            onClick = { startMonth = month; startDropdownOpen = false }
                                        )
                                    }
                                }
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                SelectorCard(
                                    label = "Fim",
                                    value = getMonthName(endMonth),
                                    icon = Icons.Default.Event,
                                    onClick = { endDropdownOpen = true }
                                )
                                DropdownMenu(
                                    expanded = endDropdownOpen,
                                    onDismissRequest = { endDropdownOpen = false },
                                    modifier = Modifier.width(180.dp).heightIn(max = 280.dp)
                                ) {
                                    availableMonths.forEach { month ->
                                        DropdownMenuItem(
                                            text = { Text(getMonthName(month)) },
                                            onClick = { endMonth = month; endDropdownOpen = false },
                                            enabled = month >= startMonth
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Conteúdo Dinâmico
                item {
                    Crossfade(targetState = displayData.isEmpty(), label = "Data Transition") { isEmpty ->
                        if (isEmpty) {
                            EmptyDataState()
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                ProgressChartCard(
                                    progressionData = displayData,
                                    monthBeforeProgression = displayDataBefore,
                                    currentPeriodLabel = getMonthName(endMonth),
                                    previousPeriodLabel = getMonthName(startMonth),
                                    colorBefore = Color.Red,
                                    colorAfter = Color.White
                                )
                                // Chamando o Grid refatorado
                                ProgressionStatsGrid(
                                    progressionData = displayData,
                                    primaryColor = Color.White
                                )
                                InsightsCard(progressionData = displayData)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyDataState() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.QueryStats,
                contentDescription = "Sem dados",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Nenhum dado neste período",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Você não registrou treinos para este exercício nos meses selecionados. Tente expandir sua busca.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProgressChartCard(
    progressionData: List<LoadProgressionPoint>,
    monthBeforeProgression: List<LoadProgressionPoint>,
    currentPeriodLabel: String,
    previousPeriodLabel: String,
    colorBefore: Color,
    colorAfter: Color
) {
    val labelColor = Color.White
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cs.surface.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)){
            if (progressionData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) { Text("Sem dados para exibir", color = labelColor) }
                return@Column
            }
            val labelProperties = LabelProperties(
                enabled = true,
                textStyle = TextStyle(
                    color = labelColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                padding = 16.dp,
                builder = { modifier, label, _, _->
                    Text(
                        modifier=modifier,
                        text=label,
                    )
                }
            )
            val labelHelperProperties = LabelHelperProperties(
                enabled = true,
                textStyle = MaterialTheme.typography.labelMedium.copy(
                    color = Color.White
                ),
            )

            LineChart(
                modifier = Modifier.fillMaxWidth().height(220.dp),
                labelProperties = labelProperties,
                labelHelperProperties = labelHelperProperties,
                gridProperties = GridProperties(
                    xAxisProperties = GridProperties.AxisProperties(
                        color = SolidColor(Color.White.copy(alpha = 0.3f)),
                        thickness = 1.dp,
                    ),
                    yAxisProperties = GridProperties.AxisProperties(
                        color = SolidColor(Color.White.copy(alpha = 0.3f)),
                        thickness = 1.dp,
                    )
                ),
                zeroLineProperties = ZeroLineProperties(
                    enabled = true,
                    color = SolidColor(Color.Gray.copy(alpha = 0.7f)),
                ),
                indicatorProperties = HorizontalIndicatorProperties(
                    textStyle = TextStyle(
                        color = labelColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                ),
                popupProperties = PopupProperties(
                    enabled = true,
                    textStyle = MaterialTheme.typography.labelMedium,
                    containerColor = Color.White,
                ),
                data = remember(progressionData, monthBeforeProgression) {
                    listOf(
                        Line(
                            label = previousPeriodLabel,
                            values = monthBeforeProgression.map { it.weight },
                            color = SolidColor(colorBefore),
                            curvedEdges = true,
                            drawStyle = DrawStyle.Stroke(2.dp),
                            dotProperties = DotProperties(enabled = false)
                        ),
                        Line(
                            label = currentPeriodLabel,
                            values = progressionData.map { it.weight },
                            color = SolidColor(colorAfter),
                            drawStyle = DrawStyle.Stroke(2.dp),
                            curvedEdges = true,
                            dotProperties = DotProperties(enabled = false)
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun StatCardString(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    color: Color
) {
    StatCardBase(modifier, title, value, unit, icon, color)
}

private fun computeChangePercent(progression: List<LoadProgressionPoint>): Double? {
    if (progression.size < 2) return null
    val start = progression.first().weight
    val last = progression.last().weight
    return if (start <= 0.0) null else ((last - start) / start) * 100.0
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: Double,
    unit: String,
    icon: ImageVector,
    color: Color
) {
    val animatedValue by animateFloatAsState(
        targetValue = value.toFloat(),
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "statAnimation"
    )

    // Aqui usamos o formatDecimal() que você criou com expect/actual
    StatCardBase(
        modifier = modifier,
        title = title,
        displayValue = animatedValue.toDouble().formatDecimal(),
        unit = unit,
        icon = icon,
        color = color
    )
}

@Composable
private fun StatCardBase(
    modifier: Modifier,
    title: String,
    displayValue: String,
    unit: String,
    icon: ImageVector,
    color: Color
) {
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = cs.surface.copy(alpha = 0.7f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = color)
                Spacer(Modifier.width(10.dp))
                Text(title, style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = displayValue, // Corrigido aqui
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.titleSmall,
                    color = color,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun SelectorCard(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Surface(
        tonalElevation = 1.dp,
        shape = RoundedCornerShape(12.dp),
        color = cs.surface.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = cs.primary)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, modifier = Modifier.size(16.dp), tint = cs.onSurfaceVariant)
                Spacer(Modifier.width(8.dp))
                Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1)
            }
        }
    }
}

@Composable
fun InsightsCard(progressionData: List<LoadProgressionPoint>) {
    if (progressionData.size < 2) return

    val change = computeChangePercent(progressionData)
    val suggestion = when {
        change == null -> "Dados insuficientes para gerar insights."
        change >= 8.0 -> "Bom progresso — mantenha o plano e considere aumentar volume gradualmente."
        change >= 2.0 -> "Progresso estável. Reavalie carga e variação de repetições para continuar avanço."
        else -> "Pequeno progresso. Verifique recuperação, frequência e variação de estímulo."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                "Insight",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(
                suggestion,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}