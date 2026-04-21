package com.example.presentation.screens.ui.progress

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.screens.ui.progress.components.ExerciseBottomSheet
import com.example.presentation.screens.ui.progress.components.ExerciseSelectorButton
import com.example.presentation.screens.ui.progress.components.InsightsCard
import com.example.presentation.screens.ui.progress.components.PeriodFilterChips
import com.example.presentation.screens.ui.progress.components.ProgressChartCard
import com.example.presentation.screens.ui.progress.components.ProgressScreenSkeleton
import com.example.presentation.screens.ui.progress.components.ProgressionStatsGrid
import com.example.presentation.screens.ui.progress.components.SplitTabRow
import com.example.presentation.screens.widgets.FitverseTopAppBar
import kotlinx.coroutines.launch

/**
 * Tela de Progressão de Carga — refatorada com MVVM puro.
 *
 * ## Responsabilidades desta composable
 * - Observar [ProgressUiState] via [StateFlow] da ViewModel.
 * - Escutar [ProgressUiEvent]s pontuais (snackbar, navegação).
 * - Delegar **todas** as ações ao ViewModel via [ProgressIntent].
 * - Orquestrar a animação de entrada "staggered" dos elementos.
 *
 * ## O que NÃO existe aqui
 * - Nenhuma lógica de filtragem, cálculo ou formatação.
 * - Nenhum `remember` guardando estado de negócio (apenas estados de UI pura,
 *   como `SnackbarHostState`).
 *
 * @param viewModel  ViewModel injetada (Koin/Hilt/manual).
 * @param onBack     Callback de navegação para voltar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Consome eventos pontuais (canal de entrega única)
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ProgressUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                ProgressUiEvent.NavigateBack    -> onBack()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            FitverseTopAppBar(
                title = "PROGRESSÃO",
                onBack = { viewModel.onIntent(ProgressIntent.NavigateBack) },
            )
        },
        containerColor = Color(0xFF0A0A0A), // OLED-friendly deep black
    ) { innerPadding ->
        // Transição animada entre os estados macros (Loading ↔ Success ↔ Error)
        AnimatedContent(
            targetState = uiState,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(200))
            },
            label = "progress_state_transition",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) { state ->
            when (state) {
                ProgressUiState.Loading        -> ProgressScreenSkeleton()
                is ProgressUiState.Success     -> ProgressSuccessContent(state, viewModel)
                is ProgressUiState.Error       -> ProgressErrorContent(state, viewModel)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ProgressSuccessContent — conteúdo principal com animação staggered
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Conteúdo do estado de sucesso.
 *
 * ## Pull-to-refresh (Material3 1.3.0+)
 * Usa [PullToRefreshBox] que encapsula internamente o `nestedScroll`,
 * o indicador visual e a sincronização de estado — eliminando os
 * `LaunchedEffect` de sincronização e o `Box` com `nestedScrollConnection`
 * que eram necessários na API anterior.
 *
 * ## Animação Staggered
 * Cada seção (filtros → gráfico → stats → insight) tem seu próprio
 * [Animatable] de alpha que dispara com delay crescente via `launch {}`,
 * criando o efeito "cascata" de entrada sem bloquear a coroutine principal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgressSuccessContent(
    state: ProgressUiState.Success,
    viewModel: ProgressViewModel,
) {
    // ── Animação staggered de entrada ─────────────────────────────────────
    val alphaFilters = remember { Animatable(0f) }
    val alphaChart   = remember { Animatable(0f) }
    val alphaStats   = remember { Animatable(0f) }
    val alphaInsight = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        val spec = tween<Float>(durationMillis = 400, easing = FastOutSlowInEasing)
        launch { alphaFilters.animateTo(1f, spec) }
        launch {
            kotlinx.coroutines.delay(80)
            alphaChart.animateTo(1f, spec)
        }
        launch {
            kotlinx.coroutines.delay(160)
            alphaStats.animateTo(1f, spec)
        }
        launch {
            kotlinx.coroutines.delay(240)
            alphaInsight.animateTo(1f, spec)
        }
    }

    // ── BottomSheet de exercício ──────────────────────────────────────────
    // Renderizado fora do LazyColumn para não herdar o contexto de scroll.
    if (state.isExerciseSheetOpen) {
        ExerciseBottomSheet(
            exercises = state.exercises,
            selectedExercise = state.selectedExercise,
            onExerciseSelected = {
//                viewModel.onIntent(ProgressIntent.SelectExercise(it))
            },
            onDismiss = { viewModel.onIntent(ProgressIntent.CloseExerciseSheet) },
        )
    }

    // ── PullToRefreshBox (API M3 1.3.0+) ─────────────────────────────────
    //
    // Diferenças em relação à API anterior:
    //   Antes → rememberPullToRefreshState() + Box(nestedScroll) + PullToRefreshContainer
    //   Agora → PullToRefreshBox recebe isRefreshing + onRefresh e gerencia tudo internamente.
    //
    // O indicador customizado mantém as cores do tema OLED (fundo #1A1A1A, ícone branco).
    val pullState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.onIntent(ProgressIntent.Refresh) },
        state = pullState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullState,
                isRefreshing = state.isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter),
                color = Color.White,
                containerColor = Color(0xFF1A1A1A),
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            // ── Seção 1: Split Tabs ────────────────────────────────────────
            item(key = "split_tabs") {
                SplitTabRow(
                    splits = state.splits,
                    selectedSplit = state.selectedSplit,
                    onSplitSelected = { viewModel.onIntent(ProgressIntent.SelectSplit(it)) },
                    modifier = Modifier.alpha(alphaFilters.value),
                )
                Spacer(Modifier.height(16.dp))
            }

            // ── Seção 2: Seletor de Exercício ──────────────────────────────
            item(key = "exercise_selector") {
                ExerciseSelectorButton(
                    selectedExercise = state.selectedExercise,
                    onClick = { viewModel.onIntent(ProgressIntent.OpenExerciseSheet) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .alpha(alphaFilters.value),
                )
                Spacer(Modifier.height(16.dp))
            }

            // ── Seção 3: Filtro de Período ─────────────────────────────────
            item(key = "period_filter") {
                PeriodFilterChips(
                    period = state.periodFilter,
                    onPeriodChanged = { viewModel.onIntent(ProgressIntent.UpdatePeriod(it)) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .alpha(alphaFilters.value),
                )
                Spacer(Modifier.height(20.dp))
            }

            // ── Conteúdo dinâmico: vazio ou dados ─────────────────────────
            if (state.chartLines.all { it.values.isEmpty() }) {
                item(key = "empty_state") { ProgressEmptyState() }
            } else {
                // ── Seção 4: Gráfico ───────────────────────────────────────
                item(key = "chart") {
                    ProgressChartCard(
                        chartLines = state.chartLines,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .alpha(alphaChart.value),
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // ── Seção 5: Grid de Estatísticas ──────────────────────────
                item(key = "stats_grid") {
                    ProgressionStatsGrid(
                        stats = state.stats,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .alpha(alphaStats.value),
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // ── Seção 6: Insight ───────────────────────────────────────
                item(key = "insight") {
                    InsightsCard(
                        insight = state.insight,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .alpha(alphaInsight.value),
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Estados auxiliares
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProgressEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.QueryStats,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.White.copy(alpha = 0.15f),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Nenhum dado no período",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.6f),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Você não registrou treinos para este exercício nos meses selecionados. Expanda o período ou selecione outro exercício.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.35f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ProgressErrorContent(
    state: ProgressUiState.Error,
    viewModel: ProgressViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.WifiOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.White.copy(alpha = 0.25f),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Algo deu errado",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = state.message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
        )
        if (state.canRetry) {
            Spacer(Modifier.height(24.dp))
            Button(onClick = { viewModel.onIntent(ProgressIntent.Retry) }) {
                Text("Tentar novamente")
            }
        }
    }
}