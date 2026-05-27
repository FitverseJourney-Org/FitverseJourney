package org.fitverse.presentation.ui.progress

// ─────────────────────────────────────────────────────────────────────────────
// Imports
// ─────────────────────────────────────────────────────────────────────────────

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.StackedBarChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.ui.progress.viewmodel.ProgressViewModel
import org.fitverse.presentation.widgets.FVScreenHeader
import org.fitverse.presentation.widgets.FitverseTopAppBar

// =============================================================================
// Card-level accent colors  — one visual identity per card
// =============================================================================

private val AccentCarga    get() = FitColors.Accent   // lime  #A3E635
private val AccentVolume   = Color(0xFF5B5EFF)         // indigo
private val AccentCalorias = Color(0xFFFF5C3D)         // orange-red
private val AccentCardio   = Color(0xFF00C9FF)         // cyan
private val AccentConsist  = Color(0xFFFFCC00)         // amber

// =============================================================================
// Marco models  (TODO: move to ProgressUiState.kt)
// =============================================================================

/**
 * Full state for the gamification track sub-card.
 * @param sectionLabel  E.g., "MARCOS", "DISTÂNCIA ACUMULADA", "MARCOS DE STREAK".
 * @param currentCount  Milestones reached.
 * @param totalCount    Total milestones.
 * @param totalXpLabel  Badge text (e.g., "+1900 XP").
 * @param milestones    Ordered list of milestones.
 * @param progressFrac  Exact position [0..1] on the track.
 * @param hintBoldText  Bold part of hint (e.g., "13.6t", "7d").
 * @param hintXpText    Accent part of hint (e.g., "+3.0k XP").
 */
data class MarcoInfo(
    val sectionLabel  : String,
    val currentCount  : Int,
    val totalCount    : Int,
    val totalXpLabel  : String,
    val milestones    : List<MarcoMilestone>,
    val progressFrac  : Float,
    val hintBoldText  : String,
    val hintXpText    : String,
)

// =============================================================================
// StateHolder
// =============================================================================

@Stable
class ProgressionStateHolder(
    val snackbarHostState : SnackbarHostState,
    val listState         : LazyListState,
)

@Composable
fun rememberProgressionStateHolder(
    snackbarHostState : SnackbarHostState = remember { SnackbarHostState() },
    listState         : LazyListState     = rememberLazyListState(),
): ProgressionStateHolder = remember(snackbarHostState, listState) {
    ProgressionStateHolder(snackbarHostState, listState)
}

// =============================================================================
// Root — ViewModel boundary
// =============================================================================

@Composable
fun ProgressionRoot(
    viewModel : ProgressViewModel,
    onBack    : () -> Unit,
    modifier  : Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val holder  = rememberProgressionStateHolder()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ProgressUiEvent.NavigateBack -> onBack()
                is ProgressUiEvent.ShowSnackbar -> holder.snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    ProgressionScreen(uiState = uiState, onIntent = viewModel::onIntent, holder = holder, modifier = modifier)
}

// =============================================================================
// Screen — pure, previewable
// =============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressionScreen(
    uiState  : ProgressUiState,
    onIntent : (ProgressIntent) -> Unit,
    holder   : ProgressionStateHolder = rememberProgressionStateHolder(),
    modifier : Modifier = Modifier,
) {
    Scaffold(
        modifier            = modifier,
        containerColor      = FitColors.Bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost        = { SnackbarHost(holder.snackbarHostState) },
        topBar = {
            FitverseTopAppBar(
                title = "Progressão",
                subtitle = {
                    Text(
                        text = "Sua evolução em números",
                        style = MaterialTheme.typography.labelSmall.copy(color = FitColors.TextMuted),
                    )
                },
                onBack = { onIntent(ProgressIntent.NavigateBack) },
            )
        },
    ) { padding ->
        when (val s = uiState) {
            is ProgressUiState.Loading -> ProgressionSkeleton(Modifier.fillMaxSize().padding(padding))
            is ProgressUiState.Error   -> ProgressionError(
                message  = s.message,
                canRetry = s.canRetry,
                onRetry  = { onIntent(ProgressIntent.Retry) },
                modifier = Modifier.fillMaxSize().padding(padding),
            )
            is ProgressUiState.Success -> ProgressionContent(
                state    = s,
                onIntent = onIntent,
                holder   = holder,
                modifier = Modifier.fillMaxSize().padding(padding),
            )
        }
    }
}

// =============================================================================
// Content — LazyColumn
// =============================================================================

@Composable
private fun ProgressionContent(
    state    : ProgressUiState.Success,
    onIntent : (ProgressIntent) -> Unit,
    holder   : ProgressionStateHolder,
    modifier : Modifier = Modifier,
) {
    Box(modifier = modifier) {
        LazyColumn(
            state               = holder.listState,
            modifier            = Modifier.fillMaxSize(),
            contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "period") {
                PeriodFilterRow(
                    selected = state.period,
                    onSelect = { onIntent(ProgressIntent.SelectPeriod(it)) },
                )
            }
            item(key = "rank") {
                RankCard(
                    info           = state.rank,
                    volumeXp       = state.volume.xpEarned,
                    caloriasXp     = state.calorias.xpEarned,
                    cardioXp       = state.cardio.xpEarned,
                    consistenciaXp = state.consistencia.xpEarned,
                )
            }
            item(key = "carga")        { CargaCard(info = state.carga, onExerciseClick = { onIntent(ProgressIntent.OpenExerciseSheet) }) }
            item(key = "volume")       { VolumeCard(info = state.volume) }
            item(key = "calorias")     { CaloriasCard(info = state.calorias) }
            item(key = "cardio")       { CardioCard(info = state.cardio) }
            item(key = "consistencia") { ConsistenciaCard(info = state.consistencia) }
            item(key = "bottom")       { Spacer(Modifier.height(32.dp)) }
        }

        if (state.isRefreshing) {
            LinearProgressIndicator(
                modifier   = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                color      = FitColors.Accent,
                trackColor = Color.Transparent,
            )
        }
    }
}

// =============================================================================
// Period Filter Row
// =============================================================================

@Composable
private fun PeriodFilterRow(selected: PeriodFilter, onSelect: (PeriodFilter) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        PeriodFilter.entries.forEach { filter ->
            val isOn = filter == selected
            FilterChip(
                selected = isOn,
                onClick  = { onSelect(filter) },
                label    = {
                    Text(filter.label, fontSize = 12.sp, fontWeight = if (isOn) FontWeight.ExtraBold else FontWeight.Medium)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = FitColors.Accent,
                    selectedLabelColor     = Color.Black,
                    containerColor         = FitColors.Surface,
                    labelColor             = FitColors.TextMuted,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled             = true,
                    selected            = isOn,
                    borderColor         = FitColors.Surface,
                    selectedBorderColor = Color.Transparent,
                ),
            )
        }
    }
}

// =============================================================================
// Rank Card
// =============================================================================

@Composable
private fun RankCard(
    info           : RankInfo,
    volumeXp       : Int      = 0,
    caloriasXp     : Int      = 0,
    cardioXp       : Int      = 0,
    consistenciaXp : Int      = 0,
    modifier       : Modifier = Modifier,
) {
    val animProg by animateFloatAsState(info.progressFrac, tween(1000, easing = EaseInOutCubic), label = "rank_arc")

    FitCard(modifier = modifier.fillMaxWidth(), accentColor = FitColors.Accent) {
        Column {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(100.dp)) {
                        val stroke  = 8.dp.toPx()
                        val inset   = stroke / 2f
                        val arcSize = Size(size.width - stroke, size.height - stroke)
                        val topLeft = Offset(inset, inset)
                        drawArc(FitColors.Surface, 135f, 270f, false, topLeft, arcSize, style = Stroke(stroke, cap = StrokeCap.Round))
                        if (animProg > 0f) {
                            drawArc(
                                brush     = Brush.sweepGradient(listOf(FitColors.Accent.copy(0.4f), FitColors.Accent)),
                                startAngle = 135f, sweepAngle = 270f * animProg,
                                useCenter = false, topLeft = topLeft, size = arcSize,
                                style     = Stroke(stroke, cap = StrokeCap.Round),
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(info.currentRank, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("${info.currentXp}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FitColors.Accent)
                        Text("XP", fontSize = 9.sp, color = FitColors.TextMuted)
                    }
                }

                Spacer(Modifier.width(20.dp))

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        FitLabel("NÍVEL ATUAL")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Bolt, null, tint = FitColors.Accent, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(info.currentRank, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape).background(FitColors.Surface)) {
                        Box(modifier = Modifier.fillMaxWidth(animProg).fillMaxHeight()
                            .background(Brush.horizontalGradient(listOf(FitColors.Accent.copy(0.6f), FitColors.Accent))))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            FitLabel("PRÓXIMO RANK")
                            Text(info.nextRank, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FitColors.Accent)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            FitLabel("FALTAM")
                            Text("${info.xpToNext} XP", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FitColors.TextMuted)
                        }
                    }
                }
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = FitColors.Surface, thickness = 1.dp)
            Spacer(Modifier.height(12.dp))
            CategoryXpRow(
                volumeXp     = volumeXp,
                caloriasXp   = caloriasXp,
                cardioXp     = cardioXp,
                consistenciaXp = consistenciaXp,
                modifier     = Modifier.padding(horizontal = 12.dp),
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

// =============================================================================
// Category XP Row  —  4 badges inside RankCard
// =============================================================================

@Composable
private fun CategoryXpRow(
    volumeXp      : Int,
    caloriasXp    : Int,
    cardioXp      : Int,
    consistenciaXp: Int,
    modifier      : Modifier = Modifier,
) {
    Row(
        modifier          = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CategoryXpBadge(Icons.Rounded.StackedBarChart, "VOLUME",   volumeXp,       AccentVolume,   Modifier.weight(1f))
        Box(Modifier.width(1.dp).height(36.dp).background(FitColors.Outline))
        CategoryXpBadge(Icons.Rounded.Restaurant,      "CALORIAS", caloriasXp,     AccentCalorias, Modifier.weight(1f))
        Box(Modifier.width(1.dp).height(36.dp).background(FitColors.Outline))
        CategoryXpBadge(Icons.Rounded.DirectionsRun,   "CARDIO",   cardioXp,       AccentCardio,   Modifier.weight(1f))
        Box(Modifier.width(1.dp).height(36.dp).background(FitColors.Outline))
        CategoryXpBadge(Icons.Rounded.Bolt,            "CONSIST.", consistenciaXp, AccentConsist,  Modifier.weight(1f))
    }
}

@Composable
private fun CategoryXpBadge(
    icon    : ImageVector,
    label   : String,
    xp      : Int,
    color   : Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier            = modifier.padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Icon(icon, null, tint = color.copy(alpha = 0.75f), modifier = Modifier.size(12.dp))
            Text(label.subSequence(0,3).toString(), color = FitColors.TextMuted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
        }
        Text("+${formatXp(xp)}", color = color, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
        Text("XP", color = color.copy(alpha = 0.40f), fontSize = 12.sp)
    }
}

// =============================================================================
// Carga Card  —  lime  —  compose-charts LineChart
// =============================================================================

@Composable
private fun CargaCard(info: CargaInfo, onExerciseClick: () -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(true) }

    FitCard(modifier = modifier.fillMaxWidth().animateContentSize(), accentColor = AccentCarga) {
        Column(modifier = Modifier.fillMaxWidth()){
            CollapsibleHeader(Icons.Rounded.FitnessCenter, "CARGA", AccentCarga, xpLabel = null, expanded, { expanded = !expanded })
            if (expanded) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)){
                    Spacer(Modifier.height(4.dp))
                    Text(info.exerciseName, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = FitColors.TextMuted)
                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("${info.currentLoad}", fontSize = 46.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 48.sp)
                        Spacer(Modifier.width(6.dp))
                        Text(info.unit, fontSize = 18.sp, color = FitColors.TextMuted, modifier = Modifier.padding(bottom = 6.dp))
                        Spacer(Modifier.width(10.dp))
                        TrendBadge(info.changePercent, AccentCarga)
                    }

                    Spacer(Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                            .background(FitColors.Bg).border(1.dp, FitColors.Surface, RoundedCornerShape(10.dp))
                            .clickable(onClick = onExerciseClick).padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            FitLabel("EXERCÍCIO")
                            Text(info.exerciseName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Text(info.muscleGroup, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FitColors.TextMuted)
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Default.KeyboardArrowDown, null, tint = FitColors.TextMuted, modifier = Modifier.size(18.dp))
                    }

                    Spacer(Modifier.height(16.dp))

                    val lineData = remember(info.chartPoints) {
                        listOf(Line(
                            label                   = "Carga",
                            values                  = info.chartPoints.map { it.toDouble() },
                            color                   = SolidColor(AccentCarga),
                            firstGradientFillColor  = AccentCarga.copy(alpha = 0.22f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec     = tween(1200, easing = EaseInOutCubic),
                            gradientAnimationDelay  = 500L,
                            drawStyle               = DrawStyle.Stroke(width = 2.dp),
                            curvedEdges             = true,
                        ))
                    }

                    LineChart(
                        modifier            = Modifier.fillMaxWidth().height(110.dp),
                        data                = lineData,
                        animationMode       = AnimationMode.Together(),
                        gridProperties      = GridProperties(enabled = false),
                        indicatorProperties = HorizontalIndicatorProperties(enabled = false),
                        labelProperties     = LabelProperties(enabled = false),
                    )

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = FitColors.Surface, thickness = 1.dp)
                    Spacer(Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        StatTrio("INICIAL", "${info.initialLoad}", info.unit, FitColors.TextMuted, Modifier.weight(1f))
                        StatTrio("ATUAL",   "${info.currentLoad}", info.unit, Color.White,          Modifier.weight(1f))
                        StatTrio("PR",      "${info.prLoad}",      info.unit, AccentCarga,          Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }

        }

    }
}

// =============================================================================
// Volume Card  —  indigo  —  heatmap canvas  +  MarcosSubCard
// =============================================================================

@Composable
private fun VolumeCard(info: VolumeInfo, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(true) }

    FitCard(modifier = modifier.fillMaxWidth().animateContentSize(), accentColor = AccentVolume) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CollapsibleHeader(Icons.Rounded.StackedBarChart, "VOLUME", AccentVolume, info.xpEarned.takeIf { it > 0 }?.let { "+${formatXp(it)} XP" }, expanded, { expanded = !expanded })

            if (expanded) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)){
                    Spacer(Modifier.height(12.dp))
                    Text("Carga total levantada", fontSize = 13.sp, color = FitColors.TextMuted)
                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("${info.totalTons}", fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 40.sp)
                        Spacer(Modifier.width(6.dp))
                        Text("t  ·  ${info.sessions} sessões", fontSize = 14.sp, color = FitColors.TextMuted, modifier = Modifier.padding(bottom = 4.dp))
                    }

                    Spacer(Modifier.height(18.dp))
                    VolumeHeatmap(grid = info.grid, rowLabels = info.rowLabels, accentColor = AccentVolume)
                    Spacer(Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                        Text("menos", fontSize = 9.sp, color = FitColors.TextMuted)
                        listOf(0.08f, 0.25f, 0.50f, 0.75f, 1.0f).forEach { alpha ->
                            Spacer(Modifier.width(4.dp))
                            Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(AccentVolume.copy(alpha = alpha)))
                        }
                        Spacer(Modifier.width(6.dp))
                        Text("mais", fontSize = 9.sp, color = FitColors.TextMuted)
                    }

                    info.marco?.let { marco ->
                        Spacer(Modifier.height(14.dp))
                        MarcosSubCard(marco, AccentVolume)
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

// =============================================================================
// Calorias Card  —  orange  —  compose-charts ColumnChart  +  MarcosSubCard
// =============================================================================

@Composable
private fun CaloriasCard(info: CaloriasInfo, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(true) }

    FitCard(modifier = modifier.fillMaxWidth().animateContentSize(), accentColor = AccentCalorias) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CollapsibleHeader(Icons.Rounded.Restaurant, "CALORIAS", AccentCalorias, info.xpEarned.takeIf { it > 0 }?.let { "+${formatXp(it)} XP" }, expanded, { expanded = !expanded })

            if (expanded) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)){
                    Spacer(Modifier.height(12.dp))
                    Text("Média diária", fontSize = 13.sp, color = FitColors.TextMuted)
                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("${info.avgDaily}", fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 40.sp)
                        Spacer(Modifier.width(6.dp))
                        Text("kcal", fontSize = 14.sp, color = FitColors.TextMuted, modifier = Modifier.padding(bottom = 4.dp))
                        Spacer(Modifier.width(10.dp))
                        TrendBadge(info.changePercent, AccentCalorias)
                    }

                    Spacer(Modifier.height(18.dp))

                    val maxActual = info.barValues.maxOrNull() ?: 1f
                    val barsData  = remember(info.barValues, info.goal) {
                        info.barValues.mapIndexed { i, actual ->
                            Bars(
                                label  = "",
                                values = listOf(
                                    Bars.Data(
                                        value = actual.toDouble(),
                                        color = SolidColor(if (i == info.barValues.lastIndex) AccentCalorias else AccentCalorias.copy(alpha = 0.28f + (actual / maxActual) * 0.52f)),
                                    ),
                                    Bars.Data(value = info.goal.toDouble(), color = SolidColor(FitColors.Bg)),
                                ),
                            )
                        }
                    }

                    ColumnChart(
                        modifier            = Modifier.fillMaxWidth().height(100.dp),
                        data                = barsData,
                        animationMode       = AnimationMode.Together(),
                        barProperties       = BarProperties(spacing = 2.dp, thickness = 10.dp, cornerRadius = Bars.Data.Radius.Circular(3.dp)),
                        gridProperties      = GridProperties(enabled = false),
                        indicatorProperties = HorizontalIndicatorProperties(enabled = false),
                        labelProperties     = LabelProperties(enabled = false),
                    )

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = FitColors.Surface, thickness = 1.dp)
                    Spacer(Modifier.height(14.dp))

                    Row(
                        modifier              = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(FitColors.Bg).padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        StatTrioVertical("HOJE",   "${info.today}", "kcal",  Color.White)
                        VerticalDivider()
                        StatTrioVertical("META",   "${info.goal}",  "kcal",  FitColors.TextMuted)
                        VerticalDivider()
                        StatTrioVertical("STREAK", "${info.streak}","dias",  AccentCalorias)
                    }

                    info.diasNaMeta?.let { marco ->
                        Spacer(Modifier.height(14.dp))
                        MarcosSubCard(marco, AccentCalorias)
                    }
                    Spacer(Modifier.height(14.dp))
                }

            }
        }
    }
}

// =============================================================================
// Cardio Card  —  cyan  —  compose-charts LineChart  +  MarcosSubCard
// =============================================================================

@Composable
private fun CardioCard(info: CardioInfo, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(true) }

    FitCard(modifier = modifier.fillMaxWidth().animateContentSize(), accentColor = AccentCardio) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CollapsibleHeader(Icons.Rounded.DirectionsRun, "CARDIO", AccentCardio, info.xpEarned.takeIf { it > 0 }?.let { "+${formatXp(it)} XP" }, expanded, { expanded = !expanded })
            if (expanded) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)){
                    Spacer(Modifier.height(12.dp))
                    Text("Pace médio", fontSize = 13.sp, color = FitColors.TextMuted)
                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("${info.paceMin}:${info.paceSec.toString().padStart(2, '0')}", fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 40.sp)
                        Spacer(Modifier.width(6.dp))
                        Text("/km", fontSize = 14.sp, color = FitColors.TextMuted, modifier = Modifier.padding(bottom = 4.dp))
                        Spacer(Modifier.width(10.dp))
                        TrendBadge(info.changePercent, AccentCardio, invertLogic = true)
                    }

                    Spacer(Modifier.height(18.dp))

                    val maxPace = info.chartPoints.maxOrNull() ?: 7f
                    val minPace = info.chartPoints.minOrNull() ?: 5f
                    val lineData = remember(info.chartPoints) {
                        listOf(Line(
                            label                   = "Pace",
                            values                  = info.chartPoints.map { (maxPace + minPace - it).toDouble() },
                            color                   = SolidColor(AccentCardio),
                            firstGradientFillColor  = AccentCardio.copy(alpha = 0.20f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec     = tween(1200, easing = EaseInOutCubic),
                            gradientAnimationDelay  = 500L,
                            drawStyle               = DrawStyle.Stroke(width = 2.dp),
                            curvedEdges             = true,
                        ))
                    }

                    LineChart(
                        modifier            = Modifier.fillMaxWidth().height(90.dp),
                        data                = lineData,
                        animationMode       = AnimationMode.Together(),
                        gridProperties      = GridProperties(enabled = false),
                        indicatorProperties = HorizontalIndicatorProperties(enabled = false),
                        labelProperties     = LabelProperties(enabled = false),
                    )

                    Spacer(Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        FitLabel("ZONAS DE FC")
                        Text("${(info.aerobicZoneFrac * 100).toInt()}% aeróbico", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentCardio)
                    }
                    Spacer(Modifier.height(6.dp))
                    FcZonesBar(info.aerobicZoneFrac, AccentCardio)

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = FitColors.Surface, thickness = 1.dp)
                    Spacer(Modifier.height(14.dp))

                    Row(
                        modifier              = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(FitColors.Bg).padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        StatTrioVertical("VO2 MAX",  "${info.vo2Max}",           "ml/kg/min", Color.White)
                        VerticalDivider()
                        StatTrioVertical("GANHO",    "+${info.changePercent}%",  "vs início", AccentCardio)
                        VerticalDivider()
                        StatTrioVertical("DIST.",    "${info.distKm}",           "km",        FitColors.TextMuted)
                    }

                    info.distanciaAcumulada?.let { marco ->
                        Spacer(Modifier.height(14.dp))
                        MarcosSubCard(marco, AccentCardio)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}

// =============================================================================
// Consistência Card  —  amber  —  Canvas grid  +  MarcosSubCard
// =============================================================================

@Composable
private fun ConsistenciaCard(info: ConsistenciaInfo, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(true) }

    FitCard(modifier = modifier.fillMaxWidth().animateContentSize(), accentColor = AccentConsist) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CollapsibleHeader(Icons.Rounded.LocalFireDepartment, "CONSISTÊNCIA", AccentConsist, info.xpEarned.takeIf { it > 0 }?.let { "+${formatXp(it)} XP" }, expanded, { expanded = !expanded })

            if (expanded) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)){
                    Spacer(Modifier.height(12.dp))
                    Text("Streak atual", fontSize = 13.sp, color = FitColors.TextMuted)
                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("${info.currentStreak}", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 44.sp)
                        Spacer(Modifier.width(8.dp))
                        Text("dias", fontSize = 16.sp, color = FitColors.TextMuted, modifier = Modifier.padding(bottom = 6.dp))
                        Spacer(Modifier.width(14.dp))
                        Text("recorde ${info.recordStreak}d", fontSize = 13.sp, color = FitColors.TextMuted, modifier = Modifier.padding(bottom = 6.dp))
                    }

                    Spacer(Modifier.height(18.dp))
                    FitLabel("ÚLTIMAS 5 SEMANAS")
                    Spacer(Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf("D","S","T","Q","Q","S","S").forEach { d ->
                            Text(d, fontSize = 9.sp, color = FitColors.TextMuted, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(Modifier.height(6.dp))

                    ConsistencyGrid(weekGrid = info.weekGrid, accentColor = AccentConsist)

                    info.marcosStreak?.let { marco ->
                        Spacer(Modifier.height(14.dp))
                        MarcosSubCard(marco, AccentConsist)
                    }
                    Spacer(Modifier.height(14.dp))
                }

            }
        }
    }
}

// =============================================================================
// MarcosSubCard  —  reusable gamification sub-card
// =============================================================================

@Composable
private fun MarcosSubCard(info: MarcoInfo, accentColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(FitColors.Bg)
            .border(1.dp, FitColors.Surface, RoundedCornerShape(12.dp))
            .padding(12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${info.sectionLabel} · ${info.currentCount}/${info.totalCount}",
                fontSize = 9.sp, fontWeight = FontWeight.Bold,
                color = FitColors.TextMuted, letterSpacing = 1.sp,
                modifier = Modifier.weight(1f),
            )
            Text(info.totalXpLabel, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = accentColor)
        }

        Spacer(Modifier.height(14.dp))
        MarcosTrack(info, accentColor)
        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(accentColor))
            Spacer(Modifier.width(6.dp))
            Text(
                buildAnnotatedString {
                    append("Falta ")
                    withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.White)) { append(info.hintBoldText) }
                    append(" para a próxima marca ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = accentColor)) { append(info.hintXpText) }
                },
                fontSize = 11.sp,
                color    = FitColors.TextMuted,
            )
        }
    }
}

// ─── MarcosTrack (Canvas) ─────────────────────────────────────────────────────

@Composable
private fun MarcosTrack(info: MarcoInfo, accentColor: Color, modifier: Modifier = Modifier) {
    val animProg by animateFloatAsState(info.progressFrac.coerceIn(0f, 1f), tween(900, easing = FastOutSlowInEasing), label = "marcos_track")
    val surfaceColor = FitColors.Surface
    val bgColor      = FitColors.Bg

    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxWidth().height(22.dp)) {
            val w  = size.width
            val cy = size.height / 2f
            val n  = info.milestones.size
            if (n < 2) return@Canvas

            val nodeXs = info.milestones.indices.map { i -> i / (n - 1).toFloat() * w }
            val progX  = animProg * w

            drawLine(surfaceColor, Offset(0f, cy), Offset(w, cy), 3.dp.toPx(), StrokeCap.Round)
            if (progX > 0f) drawLine(accentColor, Offset(0f, cy), Offset(progX.coerceAtMost(w), cy), 3.dp.toPx(), StrokeCap.Round)

            info.milestones.forEachIndexed { i, milestone ->
                val x            = nodeXs[i]
                val isNextTarget = !milestone.reached && (i == 0 || info.milestones[i - 1].reached)
                when {
                    milestone.reached -> drawCircle(accentColor, 5.dp.toPx(), Offset(x, cy))
                    isNextTarget      -> {
                        drawCircle(accentColor.copy(0.20f), 9.dp.toPx(), Offset(x, cy))
                        drawCircle(bgColor,      6.dp.toPx(), Offset(x, cy))
                        drawCircle(accentColor,  5.dp.toPx(), Offset(x, cy), style = Stroke(2.dp.toPx()))
                    }
                    else -> drawCircle(surfaceColor, 4.dp.toPx(), Offset(x, cy))
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            info.milestones.forEachIndexed { i, milestone ->
                Column(
                    modifier            = Modifier.weight(1f),
                    horizontalAlignment = when (i) {
                        0                         -> Alignment.Start
                        info.milestones.lastIndex -> Alignment.End
                        else                      -> Alignment.CenterHorizontally
                    },
                ) {
                    Text(
                        milestone.label,
                        fontSize   = 10.sp,
                        fontWeight = if (milestone.reached) FontWeight.ExtraBold else FontWeight.Normal,
                        color      = if (milestone.reached) accentColor else FitColors.TextMuted,
                    )
                    Text(
                        milestone.xpLabel,
                        fontSize = 9.sp,
                        color    = if (milestone.reached) accentColor.copy(0.65f) else FitColors.TextMuted.copy(0.5f),
                    )
                }
            }
        }
    }
}

// =============================================================================
// Canvas helpers
// =============================================================================

@Composable
private fun VolumeHeatmap(grid: List<List<Float>>, rowLabels: List<String>, accentColor: Color, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.End) {
            rowLabels.forEach { label ->
                Box(Modifier.height(18.dp), contentAlignment = Alignment.Center) {
                    Text(label, fontSize = 9.sp, color = FitColors.TextMuted)
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            grid.forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    row.forEach { intensity ->
                        Box(
                            modifier = Modifier.size(18.dp).clip(RoundedCornerShape(3.dp))
                                .background(if (intensity < 0.05f) FitColors.Bg else accentColor.copy(alpha = (0.10f + intensity * 0.90f).coerceIn(0f, 1f)))
                                .border(0.5.dp, FitColors.Surface, RoundedCornerShape(3.dp)),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FcZonesBar(aerobicFraction: Float, accentColor: Color, modifier: Modifier = Modifier) {
    val anim by animateFloatAsState(aerobicFraction.coerceIn(0f, 1f), tween(800, easing = FastOutSlowInEasing), label = "fc")
    Box(modifier = modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(FitColors.Surface)) {
        Box(modifier = Modifier.fillMaxWidth(0.08f).fillMaxHeight().background(Color(0xFFD4A017)))
        Box(modifier = Modifier.fillMaxWidth((0.08f + anim * 0.92f).coerceIn(0f, 1f)).fillMaxHeight()
            .background(Brush.horizontalGradient(listOf(accentColor.copy(0.5f), accentColor))))
    }
}

@Composable
private fun ConsistencyGrid(weekGrid: List<List<Boolean>>, accentColor: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        weekGrid.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                week.forEach { trained ->
                    Box(
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp).aspectRatio(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (trained) accentColor else FitColors.Bg)
                            .border(1.dp, if (trained) Color.Transparent else FitColors.Surface, RoundedCornerShape(6.dp)),
                    )
                }
            }
        }
    }
}

// =============================================================================
// Shared helpers
// =============================================================================

@Composable
private fun FitCard(
    modifier    : Modifier = Modifier,
    accentColor : Color    = Color.Transparent,
    content     : @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(FitColors.Surface)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .drawBehind {
                // Camada 1 — ambient glow horizontal (esquerda → transparente)
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(accentColor.copy(alpha = 0.05f), Color.Transparent),
                        startX = 0f,
                        endX   = size.width * 0.58f,
                    ),
                )
                // Camada 2 — radial glow no canto superior-esquerdo
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.07f),
                            accentColor.copy(alpha = 0.02f),
                            Color.Transparent,
                        ),
                        center = Offset(x = 0f, y = 0f),
                        radius = size.width * 0.72f,
                    ),
                )
                // Camada 3 — sombra de profundidade na base
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.22f)),
                        startY = size.height * 0.65f,
                        endY   = size.height,
                    ),
                )
                // Camada 4 — strip sólido na borda esquerda
                drawRect(
                    color   = accentColor,
                    topLeft = Offset.Zero,
                    size    = Size(4.dp.toPx(), size.height),
                )
            },
    ) { content() }
}

@Composable
private fun FitLabel(text: String, modifier: Modifier = Modifier) {
    Text(text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FitColors.TextMuted, letterSpacing = 1.sp, modifier = modifier)
}

@Composable
private fun CollapsibleHeader(
    icon     : ImageVector,
    label    : String,
    accent   : Color,
    xpLabel  : String?,
    expanded : Boolean,
    onToggle : () -> Unit,
    modifier : Modifier = Modifier,
) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onToggle).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = accent, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = FitColors.TextMuted, letterSpacing = 1.sp, modifier = Modifier.weight(1f))
        if (xpLabel != null) {
            XpBadge(xpLabel, accent)
            Spacer(Modifier.width(8.dp))
        }
        Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = FitColors.TextMuted, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun StatTrio(label: String, value: String, unit: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        FitLabel(label)
        Spacer(Modifier.height(3.dp))
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(unit,  fontSize = 10.sp, color = FitColors.TextMuted)
    }
}

@Composable
private fun StatTrioVertical(label: String, value: String, unit: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FitLabel(label)
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(unit,  fontSize = 10.sp, color = FitColors.TextMuted)
    }
}

@Composable
private fun VerticalDivider() {
    Box(modifier = Modifier.height(42.dp).width(1.dp).background(FitColors.Surface))
}

@Composable
private fun TrendBadge(percent: Int, accentColor: Color, invertLogic: Boolean = false, modifier: Modifier = Modifier) {
    val positive = if (invertLogic) percent <= 0 else percent >= 0
    val color    = if (positive) accentColor else Color(0xFFEF4444)
    val icon     = if (positive) Icons.Default.TrendingUp else Icons.Default.TrendingDown

    Row(
        modifier = modifier.clip(RoundedCornerShape(6.dp)).background(color.copy(0.12f)).padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(12.dp))
        Spacer(Modifier.width(3.dp))
        Text("${if (percent > 0) "+" else ""}$percent%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun XpBadge(text: String, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(20.dp)).background(accentColor.copy(0.14f))
            .border(1.dp, accentColor.copy(0.30f), RoundedCornerShape(20.dp)).padding(horizontal = 9.dp, vertical = 3.dp),
    ) {
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = accentColor)
    }
}

private fun formatXp(xp: Int): String = if (xp >= 1_000) "${(xp / 100) / 10.0}k".replace(".0k", "k") else "$xp"

// =============================================================================
// Loading Skeleton
// =============================================================================

@Composable
private fun ProgressionSkeleton(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue  = 0.2f, targetValue = 0.5f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "shimmer_alpha",
    )
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.fillMaxWidth(0.55f).height(36.dp).clip(RoundedCornerShape(20.dp)).background(FitColors.Surface.copy(alpha = alpha)))
        listOf(100.dp, 220.dp, 260.dp, 230.dp, 290.dp, 240.dp).forEach { h ->
            Box(modifier = Modifier.fillMaxWidth().height(h).clip(RoundedCornerShape(16.dp)).background(FitColors.Surface.copy(alpha = alpha)))
        }
    }
}

// =============================================================================
// Error State
// =============================================================================

@Composable
private fun ProgressionError(message: String, canRetry: Boolean, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(modifier = Modifier.size(72.dp).clip(CircleShape).background(Color(0xFFEF4444).copy(0.12f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Warning, null, tint = Color(0xFFEF4444), modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text("Algo deu errado", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(Modifier.height(8.dp))
        Text(message, fontSize = 14.sp, color = FitColors.TextMuted, textAlign = TextAlign.Center)
        if (canRetry) {
            Spacer(Modifier.height(28.dp))
            Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = FitColors.Accent), shape = RoundedCornerShape(12.dp)) {
                Text("Tentar novamente", color = Color.Black, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}