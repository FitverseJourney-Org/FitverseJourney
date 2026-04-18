package com.example.presentation.screens.ui.progress

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.progress.ProgressionData
import com.example.domain.model.progress.calculateProgressionStats
import com.example.domain.usecase.progression.BuildProgressionInsightUseCase
import com.example.domain.usecase.progression.GetExercisesByTrainingSplitUseCase
import com.example.domain.usecase.progression.GetProgressionDataUseCase
import com.example.domain.usecase.progression.GetTrainingSplitsUseCase
import com.example.expect.DateTimeManager
import com.example.presentation.utils.MonthNames
import com.example.presentation.utils.validatePeriod
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.Line
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel da ProgressScreen — versão final com Clean Architecture.
 *
 * ## Fluxo de dados reativo
 *
 * ```
 * Repositório (Flow) → UseCase → ViewModel → UiState → UI
 *       ▲                                        │
 *       └─────────── ProgressIntent ─────────────┘
 * ```
 *
 * Cada filtro do usuário (split, exercício, período) é representado por
 * um [MutableStateFlow] interno. O estado final de UI é derivado
 * automaticamente via [combine] + [flatMapLatest], sem nenhum `remember`
 * ou filtro manual na composable.
 *
 * ## Por que flatMapLatest?
 * Quando o usuário troca de exercício, o Flow anterior (do exercício antigo)
 * é cancelado automaticamente — evitando emissões obsoletas chegando à UI.
 *
 * ## Responsabilidades
 * - Reagir a [ProgressIntent]s atualizando os filtros internos.
 * - Combinar os dados de domínio em [ProgressUiState.Success].
 * - Construir as [Line]s do gráfico aqui (não na UI) para estabilidade de referência.
 * - Despachar [ProgressUiEvent]s pontuais via [Channel].
 *
 * @param getTrainingSplitsUseCase          Recupera fichas disponíveis.
 * @param getExercisesByTrainingSplitUseCase Recupera exercícios da ficha selecionada.
 * @param getProgressionDataUseCase         Recupera dados do período atual + comparativo.
 * @param buildProgressionInsightUseCase    Gera insight textual baseado nos dados.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProgressViewModel(
    private val getTrainingSplitsUseCase: GetTrainingSplitsUseCase,
    private val getExercisesByTrainingSplitUseCase: GetExercisesByTrainingSplitUseCase,
    private val getProgressionDataUseCase: GetProgressionDataUseCase,
    private val buildProgressionInsightUseCase: BuildProgressionInsightUseCase,
) : ViewModel() {

    // ── Saídas públicas imutáveis ─────────────────────────────────────────────

    private val _uiState = MutableStateFlow<ProgressUiState>(ProgressUiState.Loading)
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProgressUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // ── Filtros internos — cada um é um Flow independente ────────────────────
    // Mutations aqui re-trigam automaticamente os pipelines combine/flatMapLatest.

    private val _selectedSplit = MutableStateFlow("")
    private val _selectedExerciseId = MutableStateFlow<String?>(null)
    private val _periodFilter = MutableStateFlow(PeriodFilter())
    private val _isSheetOpen = MutableStateFlow(false)
    private val _currentYear = MutableStateFlow(getCurrentYear())

    // ── Pipelines reativos ────────────────────────────────────────────────────

    /**
     * Flow de fichas disponíveis.
     * Quando a lista muda (ex.: usuário cria ficha nova), o UiState atualiza
     * automaticamente sem nenhuma ação manual.
     */
    private val splitsFlow = getTrainingSplitsUseCase()

    /**
     * Flow de exercícios da ficha selecionada.
     * [flatMapLatest] cancela o Flow anterior ao trocar de ficha,
     * garantindo que exercícios de fichas antigas não apareçam.
     */
    private val exercisesFlow = _selectedSplit
        .flatMapLatest { split -> getExercisesByTrainingSplitUseCase(split) }

    /**
     * Flow de dados de progressão.
     * Reativa sempre que exercício, período ou ano muda.
     * [flatMapLatest] descarta emissões do exercício anterior ao trocar.
     */
    private val progressionFlow = combine(
        _selectedExerciseId,
        _periodFilter,
        _currentYear,
    ) { exerciseId, period, year -> Triple(exerciseId, period, year) }
        .flatMapLatest { (exerciseId, period, year) ->
            if (exerciseId == null) {
                kotlinx.coroutines.flow.flowOf(Result.success(
                    ProgressionData(
                        emptyList(),
                        emptyList()
                    )
                ))
            } else {
                getProgressionDataUseCase(
                    exerciseId = exerciseId,
                    startMonth = period.startMonth,
                    endMonth = period.endMonth,
                    currentYear = year,
                )
            }
        }

    // ── Inicialização ─────────────────────────────────────────────────────────

    init {
        observeStateUpdates()
        loadInitialSplit()
    }

    /**
     * Pipeline principal: combina todos os flows em um único [ProgressUiState].
     *
     * Usa [combine] de 4 argumentos — emite sempre que qualquer um dos flows emite.
     * A UI re-renderiza apenas o que mudou (Compose smart recomposition).
     */
    private fun observeStateUpdates() {
        combine(
            splitsFlow,
            exercisesFlow,
            progressionFlow,
            _isSheetOpen,
        ) { splits, exercises, progressionResult, isSheetOpen ->

            progressionResult.fold(
                onSuccess = { progressionData ->
                    val currentList = progressionData.current
                    val stats = calculateProgressionStats(currentList)
                    val insight = buildProgressionInsightUseCase(currentList)
                    val chartLines = buildChartLines(progressionData)
                    val currentExercise = exercises.find { it.id == _selectedExerciseId.value }
                        ?: exercises.firstOrNull()

                    // Auto-seleciona o primeiro exercício se necessário
                    if (_selectedExerciseId.value == null && currentExercise != null) {
                        _selectedExerciseId.value = currentExercise.id
                    }

                    ProgressUiState.Success(
                        splits = splits,
                        selectedSplit = _selectedSplit.value,
                        exercises = exercises,
                        selectedExercise = currentExercise,
                        isExerciseSheetOpen = isSheetOpen,
                        periodFilter = _periodFilter.value,
                        chartLines = chartLines,
                        stats = stats,
                        insight = insight,
                        isRefreshing = false,
                    )
                },
                onFailure = { throwable ->
                    ProgressUiState.Error(
                        message = throwable.message ?: "Erro ao carregar progressão.",
                        canRetry = true,
                    )
                },
            )
        }
            .catch { throwable ->
                _uiState.value = ProgressUiState.Error(throwable.message ?: "Erro inesperado.")
            }
            .onEach { state -> _uiState.value = state }
            .launchIn(viewModelScope)
    }

    /** Carrega a primeira ficha disponível ao iniciar. */
    private fun loadInitialSplit() {
        viewModelScope.launch {
            splitsFlow.collect { splits ->
                if (_selectedSplit.value.isEmpty() && splits.isNotEmpty()) {
                    _selectedSplit.value = splits.first()
                }
            }
        }
    }

    // ── Entry point único para ações da UI ───────────────────────────────────

    /**
     * Único método público de mutação — a UI nunca acessa outros métodos.
     * O `when` exaustivo garante que novos [ProgressIntent]s quebrem compilação
     * se não forem tratados aqui.
     */
    fun onIntent(intent: ProgressIntent) {
        when (intent) {
            is ProgressIntent.SelectSplit -> {
                _selectedSplit.value = intent.split
                _selectedExerciseId.value = null // reset: exercisesFlow re-emite e auto-seleciona
            }
            is ProgressIntent.SelectExercise -> {
                _selectedExerciseId.value = intent.exercise.id
                _isSheetOpen.value = false
            }
            is ProgressIntent.OpenExerciseSheet  -> _isSheetOpen.value = true
            is ProgressIntent.CloseExerciseSheet -> _isSheetOpen.value = false
            is ProgressIntent.UpdatePeriod -> {
                val (validStart, validEnd) = validatePeriod(
                    intent.filter.startMonth,
                    intent.filter.endMonth,
                )
                _periodFilter.value = PeriodFilter(validStart, validEnd)
            }
            is ProgressIntent.Refresh -> {
                // Força re-emissão atualizando o ano (re-trigga progressionFlow)
                _currentYear.value = getCurrentYear()
                (_uiState.value as? ProgressUiState.Success)?.let { current ->
                    _uiState.value = current.copy(isRefreshing = true)
                }
            }
            is ProgressIntent.Retry -> {
                _uiState.value = ProgressUiState.Loading
                _selectedSplit.value = "" // re-trigga toda a cadeia
                loadInitialSplit()
            }
            is ProgressIntent.NavigateBack -> {
                viewModelScope.launch { _events.send(ProgressUiEvent.NavigateBack) }
            }
        }
    }

    // ── Construção de dados de apresentação ───────────────────────────────────

    /**
     * Constrói as [Line]s para a biblioteca de gráficos.
     *
     * Isolado na ViewModel para que a referência ao objeto mude **apenas**
     * quando os dados mudam — nunca por recomposição de UI não relacionada.
     */
    private fun buildChartLines(data: ProgressionData): List<Line> = buildList {
        if (data.previous.isNotEmpty()) {
            add(
                Line(
                    label = MonthNames.full(_periodFilter.value.startMonth),
                    values = data.previous.map { it.weight },
                    color = SolidColor(Color(0xFFEF5350)),
                    curvedEdges = true,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                    dotProperties = DotProperties(enabled = false),
                )
            )
        }
        if (data.current.isNotEmpty()) {
            add(
                Line(
                    label = MonthNames.full(_periodFilter.value.endMonth),
                    values = data.current.map { it.weight },
                    color = SolidColor(Color.White),
                    curvedEdges = true,
                    drawStyle = DrawStyle.Stroke(width = 2.5.dp),
                    dotProperties = DotProperties(enabled = false),
                )
            )
        }
    }

    // ── Utilitários ───────────────────────────────────────────────────────────

    private fun getCurrentYear(): Int = DateTimeManager.getCurrentYear()
}