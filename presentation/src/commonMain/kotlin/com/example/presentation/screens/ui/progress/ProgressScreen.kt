package com.example.presentation.screens.ui.progress

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.DateTimeManager
import com.example.expect.PlatformDate
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.LayeredComponent
import com.patrykandpatrick.vico.compose.common.MarkerCornerBasedShape
import com.patrykandpatrick.vico.compose.common.component.ShapeComponent
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties


private fun getMonthName(month: String): String {
    return when (month) {
        "1" -> "Janeiro"
        "2" -> "Fevereiro"
        "3" -> "Março"
        "4" -> "Abril"
        "5" -> "Maio"
        "6" -> "Junho"
        "7" -> "Julho"
        "8" -> "Agosto"
        "9" -> "Setembro"
        "10" -> "Outubro"
        "11" -> "Novembro"
        "12" -> "Dezembro"
        else -> month
    }
}

// 1. Modificamos o modelo para incluir a qual ficha o exercício pertence
@Immutable
data class Exercise(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val trainingSplit: String // Ex: "Treino A", "Treino B", "Treino C"
)

@Immutable
data class LoadProgressionPoint(
    val date: PlatformDate,
    val weight: Double,
    val estimatedOneRm: Double
)

// Mock atualizado para demonstrar o filtro
val mockSupinoProgression = listOf(
    LoadProgressionPoint(
        date = DateTimeManager.create(10, 1, 2026),
        weight = 60.0,
        estimatedOneRm = 72.0
    ),
    LoadProgressionPoint(
        date = DateTimeManager.create(24, 1, 2026),
        weight = 62.5,
        estimatedOneRm = 75.0
    ),
    LoadProgressionPoint(
        date = DateTimeManager.create(7, 2, 2026),
        weight = 65.0,
        estimatedOneRm = 78.0
    ),
    LoadProgressionPoint(
        date = DateTimeManager.create(21, 2, 2026),
        weight = 65.0,
        estimatedOneRm = 80.0
    ),
    LoadProgressionPoint(
        date = DateTimeManager.create(7, 3, 2026),
        weight = 70.0,
        estimatedOneRm = 84.0
    )
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    exercises: List<Exercise>,
    currentProgression: List<LoadProgressionPoint>,
    monthbeforeProgression: List<LoadProgressionPoint>,
    toBack: () -> Unit
) {
    // --- 1. Lógica de Filtro: Treino e Exercício ---
    val availableSplits = remember(exercises) {
        exercises.map { it.trainingSplit }.distinct().sorted()
    }
    var selectedSplit by remember { mutableStateOf(availableSplits.firstOrNull() ?: "") }
    var splitDropdownOpen by remember { mutableStateOf(false) }

    val filteredExercises = remember(selectedSplit, exercises) {
        exercises.filter { it.trainingSplit == selectedSplit }
    }
    var selectedExercise by remember(filteredExercises) { mutableStateOf(filteredExercises.firstOrNull()) }
    var exerciseDropdownOpen by remember { mutableStateOf(false) }

    // --- 2. Lógica de Filtro: Período (Início e Fim) ---
    // Agora a lista contém todos os meses de 1 a 12 fixamente
    val availableMonths = remember { (1..12).toList() }

    var startMonth by remember { mutableIntStateOf(1) } // Janeiro por padrão
    var endMonth by remember { mutableIntStateOf(12) }  // Dezembro por padrão

    var startDropdownOpen by remember { mutableStateOf(false) }
    var endDropdownOpen by remember { mutableStateOf(false) }

    // Filtragem dos dados baseada no range [startMonth..endMonth] e também no exercício selecionado (boa prática)
    val displayData = remember(startMonth, endMonth, currentProgression) {
        currentProgression.filter { it.date.month in startMonth..endMonth }
    }

    val displayDataBefore = remember(startMonth, endMonth, monthbeforeProgression) {
        monthbeforeProgression.filter { it.date.month in startMonth..endMonth }
    }

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Progressão", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = toBack) { Icon(Icons.Default.ChevronLeft, null) }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        AnimatedVisibility(visible = isVisible, modifier = Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // CABEÇALHO DE FILTROS
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // PRIMEIRA LINHA: Ficha e Exercício
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(modifier = Modifier.weight(.5f)) {
                                SelectorCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = "Ficha",
                                    value = selectedSplit,
                                    icon = Icons.Default.ListAlt,
                                    onClick = { splitDropdownOpen = true }
                                )
                                DropdownMenu(modifier = Modifier.fillMaxWidth(.30f), expanded = splitDropdownOpen, onDismissRequest = { splitDropdownOpen = false }) {
                                    availableSplits.forEach { split ->
                                        DropdownMenuItem(text = { Text(split) }, onClick = { selectedSplit = split; splitDropdownOpen = false })
                                    }
                                }
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                SelectorCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = "Exercício",
                                    value = selectedExercise?.name ?: "Selecione",
                                    icon = Icons.Default.FitnessCenter,
                                    onClick = { exerciseDropdownOpen = true }
                                )
                                DropdownMenu(modifier = Modifier.fillMaxWidth(.60f), expanded = exerciseDropdownOpen, onDismissRequest = { exerciseDropdownOpen = false }) {
                                    filteredExercises.forEach { ex ->
                                        DropdownMenuItem(text = { Text(ex.name) }, onClick = { selectedExercise = ex; exerciseDropdownOpen = false })
                                    }
                                }
                            }
                        }

                        // SEGUNDA LINHA: Período Inicial e Final
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                SelectorCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = "Início",
                                    value = getMonthName(startMonth.toString()),
                                    icon = Icons.Default.CalendarMonth,
                                    onClick = { startDropdownOpen = true }
                                )
                                DropdownMenu(modifier = Modifier.fillMaxWidth(.45f), expanded = startDropdownOpen, onDismissRequest = { startDropdownOpen = false }) {
                                    availableMonths.forEach { month ->
                                        DropdownMenuItem(
                                            text = { Text(getMonthName(month.toString())) },
                                            onClick = { startMonth = month; startDropdownOpen = false }
                                        )
                                    }
                                }
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                SelectorCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = "Fim",
                                    value = getMonthName(endMonth.toString()),
                                    icon = Icons.Default.Event,
                                    onClick = { endDropdownOpen = true }
                                )
                                DropdownMenu(modifier = Modifier.fillMaxWidth(.45f), expanded = endDropdownOpen, onDismissRequest = { endDropdownOpen = false }) {
                                    availableMonths.forEach { month ->
                                        DropdownMenuItem(
                                            text = { Text(getMonthName(month.toString())) },
                                            onClick = { endMonth = month; endDropdownOpen = false },
                                            enabled = month >= startMonth // Impede selecionar fim menor que início
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // CONTEÚDO DINÂMICO (Gráficos ou Empty State)
                item {
                    // Crossfade cria uma transição suave entre a tela vazia e os gráficos
                    Crossfade(
                        targetState = displayData.isEmpty(),
                        label = "Data Transition"
                    ) { isEmpty ->
                        if (isEmpty) {
                            EmptyDataState()
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                ProgressChartCard(
                                    // Atenção: Certifique-se de que o chart receba os dados filtrados (displayData) se desejar que o gráfico obedeça o filtro
                                    progressionData = displayData,
                                    monthbeforeProgression = displayDataBefore
                                )
                                KeyStatsRow(progressionData = displayData)
                                InsightsCard(progressionData = displayData)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Componente de UX para quando não há dados ---
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
    monthbeforeProgression: List<LoadProgressionPoint>
) {
    val labelColor = Color.White

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
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
                data = remember(progressionData, monthbeforeProgression) {
                    listOf(
                        Line(
                            label = "Mês Anterior",
                            values = monthbeforeProgression.map { it.weight },
                            color = SolidColor(Color.White),
                            curvedEdges = true,
                            drawStyle = DrawStyle.Stroke(2.dp),
                            dotProperties = DotProperties(enabled = false)
                        ),
                        Line(
                            label = "Carga Atual",
                            values = progressionData.map { it.weight },
                            color = SolidColor(Color.Yellow),
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

// ---------- Funções auxiliares de cálculo ----------
private fun computeChangePercent(progression: List<LoadProgressionPoint>): Double? {
    if (progression.size < 2) return null
    val start = progression.first().weight
    val last = progression.last().weight
    return if (start <= 0.0) null else ((last - start) / start) * 100.0
}

private fun computePr(progression: List<LoadProgressionPoint>): LoadProgressionPoint? {
    return progression.maxByOrNull { it.weight }
}
@Composable
fun ChartMarkerComponent(
    value: String,
    modifier: Modifier = Modifier
) {
    // A caixa preta arredondada do marker
    Surface(
        color = Color.Black.copy(alpha = 0.85f), // Fundo preto levemente transparente
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone opcional, mas vamos focar no texto como na imagem
            Text(
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ---------- Componente: cards de stats ----------
@Composable
fun KeyStatsRow(progressionData: List<LoadProgressionPoint>) {
    if (progressionData.isEmpty()) return

    val latest = progressionData.last()
    val pr = computePr(progressionData)

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Recorde Pessoal",
            value = pr?.weight?.toInt() ?: 0,
            unit = "kg",
            icon = Icons.Default.TrendingUp,
            color = Color(0xFFFFD700)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "1RM Estimada",
            value = latest.estimatedOneRm.toInt(),
            unit = "kg",
            icon = Icons.Default.FitnessCenter,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: Int,
    unit: String,
    icon: ImageVector,
    color: Color
) {
    val animatedValue by animateIntAsState(
        targetValue = value,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "statCount"
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = "$title icon",
                    modifier = Modifier.size(18.dp),
                    tint = color
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$animatedValue",
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
    Surface(
        tonalElevation = 1.dp,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = modifier.clip(RoundedCornerShape(12.dp)).clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

// ---------- Componente: insights simples ----------
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




@Composable
internal fun rememberMarker(
    valueFormatter: DefaultCartesianMarker.ValueFormatter =
        DefaultCartesianMarker.ValueFormatter.default(),
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = MarkerCornerBasedShape(CircleShape)
    val labelBackground = rememberShapeComponent(
        fill = Fill(MaterialTheme.colorScheme.background),
        shape = labelBackgroundShape,
        strokeFill = Fill(MaterialTheme.colorScheme.outline),
        strokeThickness = 1.dp,
    )

    val label = rememberTextComponent(
        style =
            TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
            ),
        padding = Insets(8.dp, 4.dp),
        background = labelBackground,
        minWidth = TextComponent.MinWidth.fixed(40.dp),
    )

    val indicatorFrontComponent = rememberShapeComponent(Fill(MaterialTheme.colorScheme.surface), CircleShape)

    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
        indicator =
            if (showIndicator) {
                { color ->
                    LayeredComponent(
                        back = ShapeComponent(Fill(color.copy(alpha = 0.15f)), CircleShape),
                        front =
                            LayeredComponent(
                                back = ShapeComponent(fill = Fill(color), shape = CircleShape),
                                front = indicatorFrontComponent,
                                padding = Insets(5.dp),
                            ),
                        padding = Insets(10.dp),
                    )
                }
            } else {
                null
            },
        indicatorSize = 36.dp,
    )
}