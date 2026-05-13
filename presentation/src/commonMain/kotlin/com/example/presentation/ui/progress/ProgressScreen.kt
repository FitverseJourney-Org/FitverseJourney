package com.example.presentation.ui.progress

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FVExtension
import com.example.presentation.ui.progress.components.ExerciseBottomSheet
import com.example.presentation.ui.progress.components.ExerciseSelectorButton
import com.example.presentation.ui.progress.components.InsightsCard
import com.example.presentation.ui.progress.components.PeriodFilterChips
import com.example.presentation.ui.progress.components.ProgressChartCard
import com.example.presentation.ui.progress.components.ProgressScreenSkeleton
import com.example.presentation.ui.progress.components.ProgressionStatsGrid
import com.example.presentation.ui.progress.components.SplitTabRow
import com.example.presentation.ui.progress.viewmodel.ProgressViewModel
import com.example.presentation.widgets.FVCard
import com.example.presentation.widgets.FVScreenHeader
import com.example.presentation.widgets.FVSectionLabel

// ─────────────────────────────────────────────────────────────────────────────
// Root — recebe o ViewModel, observa eventos pontuais
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ProgressionRoot(
    viewModel : ProgressViewModel,
    onBack    : () -> Unit,
    modifier  : Modifier = Modifier,
) {
    val uiState           by viewModel.uiState.collectAsState()
    val snackbarHostState  = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ProgressUiEvent.NavigateBack  -> onBack()
                is ProgressUiEvent.ShowSnackbar  -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    ProgressionScreen(
        uiState           = uiState,
        onIntent          = viewModel::onIntent,
        snackbarHostState = snackbarHostState,
        modifier          = modifier,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Screen — puro, previewável, sem referência ao ViewModel
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ProgressionScreen(
    uiState           : ProgressUiState,
    onIntent          : (ProgressIntent) -> Unit,
    snackbarHostState : SnackbarHostState = remember { SnackbarHostState() },
    modifier          : Modifier = Modifier,
) {
    Scaffold(
        modifier            = modifier,
        containerColor      = FVExtension.bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost        = { SnackbarHost(snackbarHostState) },
        topBar = {
            FVScreenHeader(
                title  = "PROGRESSÃO",
                sub    = "Sua evolução em números",
                onBack = { onIntent(ProgressIntent.NavigateBack) },
            )
        },
    ) { paddingValues ->
        when (val state = uiState) {
            is ProgressUiState.Loading -> {
                ProgressScreenSkeleton(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }

            is ProgressUiState.Error -> {
                ProgressionErrorContent(
                    message  = state.message,
                    canRetry = state.canRetry,
                    onRetry  = { onIntent(ProgressIntent.Retry) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }

            is ProgressUiState.Success -> {
                ProgressionSuccessContent(
                    state    = state,
                    onIntent = onIntent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
                if (state.isExerciseSheetOpen) {
                    ExerciseBottomSheet(
                        exercises          = state.exercises,
                        selectedExercise   = state.selectedExercise,
                        onExerciseSelected = { onIntent(ProgressIntent.SelectExercise(it)) },
                        onDismiss          = { onIntent(ProgressIntent.CloseExerciseSheet) },
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Conteúdo de sucesso
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProgressionSuccessContent(
    state    : ProgressUiState.Success,
    onIntent : (ProgressIntent) -> Unit,
    modifier : Modifier = Modifier,
) {
    Column(modifier = modifier) {

        // Indicador de atualização não-bloqueante
        if (state.isRefreshing) {
            LinearProgressIndicator(
                modifier   = Modifier.fillMaxWidth(),
                color      = FVExtension.primary,
                trackColor = Color.Transparent,
            )
        }

        LazyColumn(
            modifier       = Modifier
                .weight(1f)
                .background(FVExtension.bg),
            contentPadding = PaddingValues(bottom = 48.dp),
        ) {

            // 1 ── Fichas de treino (tab row)
            item(key = "split_tabs") {
                SplitTabRow(
                    splits          = state.splits,
                    selectedSplit   = state.selectedSplit,
                    onSplitSelected = { onIntent(ProgressIntent.SelectSplit(it)) },
                )
            }

            // 2 ── Seletor de exercício
            item(key = "exercise_selector") {
                Column(
                    Modifier
                        .padding(horizontal = FVExtension.margin)
                        .padding(top = 16.dp),
                ) {
                    ExerciseSelectorButton(
                        selectedExercise = state.selectedExercise,
                        onClick          = { onIntent(ProgressIntent.OpenExerciseSheet) },
                    )
                }
            }

            // 3 ── Filtro de período (chips de mês)
            item(key = "period_filter") {
                Column(
                    Modifier
                        .padding(horizontal = FVExtension.margin)
                        .padding(top = 12.dp),
                ) {
                    PeriodFilterChips(
                        period          = state.periodFilter,
                        onPeriodChanged = { onIntent(ProgressIntent.UpdatePeriod(it)) },
                    )
                }
            }

            // 4 ── Gráfico de evolução de carga (compose-charts: linha + colunas)
            item(key = "load_chart") {
                Spacer(Modifier.height(20.dp))
                FVSectionLabel("Evolução de Carga")
                Spacer(Modifier.height(8.dp))
                Column(Modifier.padding(horizontal = FVExtension.margin)) {
                    ProgressChartCard(chartLines = state.chartLines)
                }
            }

            // 5 ── Grade de estatísticas (PR, carga atual, evolução, sessões)
            item(key = "stats_grid") {
                Spacer(Modifier.height(16.dp))
                FVSectionLabel("Estatísticas do Período")
                Spacer(Modifier.height(8.dp))
                Column(Modifier.padding(horizontal = FVExtension.margin)) {
                    ProgressionStatsGrid(
                        stats       = state.stats,
                        accentColor = FVExtension.primary,
                    )
                }
            }

            // 6 ── Insight de evolução
            item(key = "insight") {
                Spacer(Modifier.height(16.dp))
                Column(Modifier.padding(horizontal = FVExtension.margin)) {
                    InsightsCard(insight = state.insight)
                }
            }

            // 7 ── Volume semanal (barras animadas)
            item(key = "volume_chart") {
                Spacer(Modifier.height(20.dp))
                FVSectionLabel("Volume Semanal", action = "+12% vs semana ant.")
                Spacer(Modifier.height(8.dp))
                Column(Modifier.padding(horizontal = FVExtension.margin)) {
                    FVCard {
                        VolumeBarChart(
                            values   = listOf(2800f, 3500f, 4100f, 4800f, 5200f, 6100f),
                            labels   = listOf("S1", "S2", "S3", "S4", "S5", "S6"),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                        )
                    }
                }
            }

            // 8 ── Frequência de treino (heatmap 8 semanas)
            item(key = "heatmap") {
                Spacer(Modifier.height(16.dp))
                FVSectionLabel("Frequência de Treino")
                Spacer(Modifier.height(8.dp))
                Column(Modifier.padding(horizontal = FVExtension.margin)) {
                    FVCard {
                        TrainingHeatmap(Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        HeatmapLegend()
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Estado de erro com retry
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProgressionErrorContent(
    message  : String,
    canRetry : Boolean,
    onRetry  : () -> Unit,
    modifier : Modifier = Modifier,
) {
    Column(
        modifier            = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector        = Icons.Default.Warning,
            contentDescription = null,
            tint               = FVExtension.textMuted,
            modifier           = Modifier.size(48.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text       = message,
            fontSize   = 15.sp,
            color      = FVExtension.textMuted,
            textAlign  = TextAlign.Center,
            lineHeight = 22.sp,
        )
        if (canRetry) {
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors  = ButtonDefaults.buttonColors(containerColor = FVExtension.primary),
                shape   = RoundedCornerShape(FVExtension.radiusBtn),
            ) {
                Text(
                    text       = "Tentar novamente",
                    color      = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Gráfico de volume semanal (barras animadas com Canvas-free rendering)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun VolumeBarChart(
    values   : List<Float>,
    labels   : List<String>,
    modifier : Modifier = Modifier,
) {
    val maxVal = remember(values) { values.max() }

    Column(modifier = modifier) {
        Row(
            modifier              = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.Bottom,
        ) {
            values.forEachIndexed { i, v ->
                VolumeBar(
                    fraction = v / maxVal,
                    isLast   = i == values.lastIndex,
                    index    = i,
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            labels.forEachIndexed { i, label ->
                Text(
                    text       = label,
                    fontSize   = 9.sp,
                    color      = if (i == labels.lastIndex) FVExtension.primary else FVExtension.textMuted,
                    fontWeight = if (i == labels.lastIndex) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
private fun VolumeBar(fraction: Float, isLast: Boolean, index: Int) {
    val animH by animateFloatAsState(
        targetValue   = fraction,
        animationSpec = tween(600 + index * 80, easing = FastOutSlowInEasing),
        label         = "bar$index",
    )
    Box(
        modifier = Modifier
            .width(28.dp)
            .fillMaxHeight(animH)
            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            .background(
                if (isLast) FVExtension.primary
                else FVExtension.primary.copy(alpha = 0.15f + fraction * 0.45f)
            )
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Heatmap de frequência de treino (8 semanas × 7 dias)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun TrainingHeatmap(modifier: Modifier = Modifier) {
    val intensities = remember {
        List(7 * 8) { i ->
            when {
                i % 7 == 6 -> 0f
                i % 3 == 0 -> (0.3f + (i % 5) * 0.15f).coerceAtMost(1f)
                i % 5 == 0 -> 0f
                else       -> (0.1f + (i % 7) * 0.12f).coerceAtMost(1f)
            }
        }
    }

    Column(modifier = modifier) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom").forEach { d ->
                Text(
                    text     = d,
                    fontSize = 8.sp,
                    color    = FVExtension.textMuted,
                    modifier = Modifier.width(32.dp),
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        repeat(8) { week ->
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                repeat(7) { day ->
                    val intensity = intensities[week * 7 + day]
                    Box(
                        modifier = Modifier
                            .size(width = 30.dp, height = 10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (intensity == 0f) Color(0xFF1A1A22)
                                else FVExtension.primary.copy(alpha = intensity)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun HeatmapLegend() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Text(text = "Menos", fontSize = 9.sp, color = FVExtension.textMuted)
        listOf(0.08f, 0.2f, 0.5f, 1.0f).forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(FVExtension.primary.copy(alpha = alpha), RoundedCornerShape(2.dp))
            )
        }
        Text(text = "Mais", fontSize = 9.sp, color = FVExtension.textMuted)
    }
}
