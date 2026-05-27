package org.fitverse.presentation.ui.progress.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.domain.models.progression.Exercise
import org.fitverse.domain.models.progression.rank.RankDomain
import org.fitverse.domain.models.progression.volume.VolumeData
import org.fitverse.domain.models.progression.calorias.CaloriasData
import org.fitverse.domain.models.progression.cardio.CardioData
import org.fitverse.domain.models.progression.consistencia.ConsistenciaData
import org.fitverse.domain.usecase.progression.GetExercisesByTrainingSplitUseCase
import org.fitverse.domain.usecase.progression.GetProgressionDataUseCase
import org.fitverse.domain.usecase.progression.GetTrainingSplitsUseCase
import org.fitverse.domain.usecase.progression.rank.GetRankInfoUseCase
import org.fitverse.domain.usecase.progression.volume.GetVolumeDataUseCase
import org.fitverse.domain.usecase.progression.calorias.GetCaloriasDataUseCase
import org.fitverse.domain.usecase.progression.cardio.GetCardioDataUseCase
import org.fitverse.domain.usecase.progression.consistencia.GetConsistenciaDataUseCase
import org.fitverse.presentation.ui.progress.CargaInfo
import org.fitverse.presentation.ui.progress.CaloriasInfo
import org.fitverse.presentation.ui.progress.CardioInfo
import org.fitverse.presentation.ui.progress.ConsistenciaInfo
import org.fitverse.presentation.ui.progress.PeriodFilter
import org.fitverse.presentation.ui.progress.ProgressIntent
import org.fitverse.presentation.ui.progress.ProgressUiEvent
import org.fitverse.presentation.ui.progress.ProgressUiState
import org.fitverse.presentation.ui.progress.RankInfo
import org.fitverse.presentation.ui.progress.VolumeInfo
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.fitverse.domain.models.progression.ProgressionData

/**
 * ViewModel da ProgressionScreen — versão refatorada para os 8 cards da tela.
 *
 * ## Arquitetura reativa
 *
 * ```
 * Repositório (Flow) → UseCase → ViewModel → ProgressUiState → Screen
 *        ▲                                          │
 *        └──────────── ProgressIntent ──────────────┘
 * ```
 *
 * Cada fonte de dados é um Flow independente. O estado final da UI é derivado
 * automaticamente via [combine], sem nenhuma lógica de merge manual na composable.
 *
 * ## Estratégia de erros por card
 *
 * Cada Flow de domínio emite [Result]. Falhas parciais (ex.: cardio indisponível)
 * não derrubam a tela inteira — apenas o card afetado exibe um estado degradado.
 * Apenas uma falha no carregamento principal (rank + carga) eleva para [ProgressUiState.Error].
 *
 * ## Por que flatMapLatest?
 *
 * Ao trocar de exercício ou período, o Flow anterior é cancelado automaticamente,
 * evitando que dados obsoletos cheguem à UI.
 *
 * @param getTrainingSplitsUseCase          Recupera fichas disponíveis.
 * @param getExercisesByTrainingSplitUseCase Exercícios da ficha selecionada.
 * @param getProgressionDataUseCase         Dados de carga do exercício + período.
 * @param getRankInfoUseCase                Rank atual e XP do usuário.
 * @param getVolumeDataUseCase              Volume total e heatmap 7×7.
 * @param getCaloriasDataUseCase            Calorias médias, histórico e streak.
 * @param getCardioDataUseCase              Pace, VO2 Max, zonas de FC.
 * @param getConsistenciaDataUseCase        Streak e grid de semanas.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProgressViewModel(
    private val getTrainingSplitsUseCase          : GetTrainingSplitsUseCase,
    private val getExercisesByTrainingSplitUseCase: GetExercisesByTrainingSplitUseCase,
    private val getProgressionDataUseCase         : GetProgressionDataUseCase,
    private val getRankInfoUseCase                : GetRankInfoUseCase,
    private val getVolumeDataUseCase              : GetVolumeDataUseCase,
    private val getCaloriasDataUseCase            : GetCaloriasDataUseCase,
    private val getCardioDataUseCase              : GetCardioDataUseCase,
    private val getConsistenciaDataUseCase        : GetConsistenciaDataUseCase,
) : ViewModel() {

    // ── Saídas públicas imutáveis ─────────────────────────────────────────────

    private val _uiState = MutableStateFlow<ProgressUiState>(ProgressUiState.Loading)
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProgressUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // ── Filtros internos ──────────────────────────────────────────────────────
    // Cada MutableStateFlow representa um parâmetro de filtro independente.
    // Mutations aqui re-trigam os pipelines combine/flatMapLatest automaticamente.

    private val _period         = MutableStateFlow(PeriodFilter.default)
    private val _selectedSplit  = MutableStateFlow<String?>(null)
    private val _selectedExId   = MutableStateFlow<String?>(null)
    private val _sheetOpen      = MutableStateFlow(false)

    // ── Pipelines de domínio ──────────────────────────────────────────────────

    /**
     * Fichas de treino disponíveis.
     * Roda continuamente — atualiza automaticamente se o usuário criar uma nova ficha.
     */
    private val splitsFlow = getTrainingSplitsUseCase()

    /**
     * Exercícios da ficha ativa.
     * [flatMapLatest] cancela o Flow anterior ao trocar de ficha.
     */
    private val exercisesFlow = _selectedSplit
        .flatMapLatest { split ->
            getExercisesByTrainingSplitUseCase(split ?: "")
        }

    /**
     * Dados de carga do exercício selecionado no período atual.
     * Re-trigado por qualquer mudança em exercício ou período.
     */
    private val cargaFlow = combine(_selectedExId, _period) { exId, period ->
        Pair(exId, period)
    }.flatMapLatest { (exId, period) ->
        getProgressionDataUseCase(
            exerciseId  = exId ?: "",
            periodDays  = period.days,
        )
    }

    /**
     * Rank e XP do usuário.
     * Não depende de filtros — emite quando os dados de gamificação atualizam.
     */
    private val rankFlow = getRankInfoUseCase()

    /**
     * Volume total levantado no período selecionado.
     */
    private val volumeFlow = _period
        .flatMapLatest { period -> getVolumeDataUseCase(periodDays = period.days) }

    /**
     * Dados de calorias no período selecionado.
     */
    private val caloriasFlow = _period
        .flatMapLatest { period -> getCaloriasDataUseCase(periodDays = period.days) }

    /**
     * Métricas de cardio no período selecionado.
     */
    private val cardioFlow = _period
        .flatMapLatest { period -> getCardioDataUseCase(periodDays = period.days) }

    /**
     * Streak e grid de consistência.
     * Independente de período — sempre mostra as últimas 5 semanas.
     */
    private val consistenciaFlow = getConsistenciaDataUseCase()

    // ── Inicialização ─────────────────────────────────────────────────────────

    init {
        autoSelectFirstSplit()
        observeStateUpdates()
    }

    /**
     * Seleciona automaticamente a primeira ficha disponível.
     * Executa uma única vez — encerra após a primeira emissão não-vazia.
     */
    private fun autoSelectFirstSplit() {
        viewModelScope.launch {
            splitsFlow.collect { splits ->
                if (_selectedSplit.value == null && splits.isNotEmpty()) {
                    _selectedSplit.value = splits.first()
                }
            }
        }
    }

    // ── Pipeline principal ────────────────────────────────────────────────────

    /**
     * Combina todos os 7 flows em um único [ProgressUiState].
     *
     * ## Estratégia de erro parcial
     *
     * - Falha em [rankFlow] ou [cargaFlow] → [ProgressUiState.Error] (críticos).
     * - Falha nos demais cards → mantém o card com dados default / empty state.
     *   O usuário pode continuar usando o restante da tela.
     *
     * Usa [combine] de até 6 argumentos (limite da API). Para 7+ flows,
     * agrupamos em etapas intermediárias com [combine] aninhado.
     */
    private fun observeStateUpdates() {
        // Etapa 1: agrupa os cards secundários (volume, calorias, cardio, consistência)
        val secondaryFlow = combine(
            volumeFlow,
            caloriasFlow,
            cardioFlow,
            consistenciaFlow,
        ) { volumeResult, caloriasResult, cardioResult, consistenciaResult ->
            SecondaryData(
                volume       = volumeResult.getOrNull(),
                calorias     = caloriasResult.getOrNull(),
                cardio       = cardioResult.getOrNull(),
                consistencia = consistenciaResult.getOrNull(),
            )
        }

        // Etapa 2: combina tudo em UiState
        combine(
            exercisesFlow,
            cargaFlow,
            rankFlow,
            _sheetOpen,
            secondaryFlow,
        ) { exercises, cargaResult, rankResult, sheetOpen, secondary ->

            // Rank é crítico — falha eleva para estado de erro
            val rankDomain = rankResult.getOrElse {
                return@combine ProgressUiState.Error(
                    message  = it.message ?: "Erro ao carregar rank.",
                    canRetry = true,
                )
            }

            // Resolve exercício selecionado e auto-seleciona o primeiro se necessário
            val currentExercise = resolveSelectedExercise(exercises)

            // Carga — estado degradado se falhar (não derruba a tela)
            val cargaInfo = cargaResult
                .getOrNull()
                ?.let { buildCargaInfo(it, currentExercise) }
                ?: CargaInfo()

            ProgressUiState.Success(
                period        = _period.value,
                rank          = buildRankInfo(rankDomain),
                carga         = cargaInfo,
                exercises     = exercises,
                volume        = secondary.volume?.let { buildVolumeInfo(it) }        ?: VolumeInfo(),
                calorias      = secondary.calorias?.let { buildCaloriasInfo(it) }    ?: CaloriasInfo(),
                cardio        = secondary.cardio?.let { buildCardioInfo(it) }        ?: CardioInfo(),
                consistencia  = secondary.consistencia?.let { buildConsistenciaInfo(it) } ?: ConsistenciaInfo(),
                isRefreshing  = false,
            )
        }
            .catch { throwable ->
                _uiState.value = ProgressUiState.Error(
                    message  = throwable.message ?: "Erro inesperado.",
                    canRetry = true,
                )
            }
            .onEach { state -> _uiState.value = state }
            .launchIn(viewModelScope)
    }

    // ── Entry point único ─────────────────────────────────────────────────────

    /**
     * Único método público de mutação.
     * O `when` exaustivo garante que novos [ProgressIntent]s
     * quebrem a compilação se não forem tratados aqui.
     */
    fun onIntent(intent: ProgressIntent) {
        when (intent) {

            is ProgressIntent.SelectPeriod -> {
                _period.value = intent.period
            }

            is ProgressIntent.SelectExercise -> {
                _selectedExId.value = intent.exercise.id
                _sheetOpen.value    = false
            }

            is ProgressIntent.OpenExerciseSheet  -> _sheetOpen.value = true
            is ProgressIntent.CloseExerciseSheet -> _sheetOpen.value = false

            is ProgressIntent.Refresh -> {
                // Marca isRefreshing sem ocultar o conteúdo atual
                _uiState.update { current ->
                    (current as? ProgressUiState.Success)?.copy(isRefreshing = true) ?: current
                }
                // Força re-emissão re-instanciando os filtros
                _period.value = _period.value
            }

            is ProgressIntent.Retry -> {
                _uiState.value     = ProgressUiState.Loading
                _selectedSplit.value = null   // re-trigga toda a cadeia
                _selectedExId.value  = null
                autoSelectFirstSplit()
            }

            is ProgressIntent.NavigateBack -> {
                viewModelScope.launch { _events.send(ProgressUiEvent.NavigateBack) }
            }
        }
    }

    // ── Resolução de exercício selecionado ────────────────────────────────────

    /**
     * Retorna o exercício atualmente selecionado ou auto-seleciona o primeiro
     * da lista quando nenhum foi escolhido explicitamente.
     */
    private fun resolveSelectedExercise(exercises: List<Exercise>): Exercise? {
        val current = exercises.find { it.id == _selectedExId.value }
        if (current == null && exercises.isNotEmpty()) {
            _selectedExId.value = exercises.first().id
            return exercises.first()
        }
        return current
    }

    // ═════════════════════════════════════════════════════════════════════════
    // Mappers Domain → UI Model
    // Isolados aqui para que a referência ao objeto mude APENAS quando os
    // dados mudarem — nunca por recomposição de UI não relacionada.
    // ═════════════════════════════════════════════════════════════════════════

    private fun buildRankInfo(domain: RankDomain): RankInfo {
        val total        = domain.currentXp + domain.xpToNext
        val progressFrac = (domain.currentXp.toFloat() / total).coerceIn(0f, 1f)
        return RankInfo(
            currentRank  = domain.rank,
            currentXp    = domain.currentXp,
            nextRank     = domain.nextRank,
            xpToNext     = domain.xpToNext,
            progressFrac = progressFrac,
        )
    }

    private fun buildCargaInfo(
        domain   : ProgressionData,
        exercise : Exercise?,
    ): CargaInfo {
        val points   = domain.current.map { it.weight.toFloat() }
        val initial  = points.firstOrNull()?.toInt() ?: 0
        val current  = points.lastOrNull()?.toInt()  ?: 0
        val pr       = domain.current.maxOfOrNull { it.weight }?.toInt() ?: current
        val change   = if (initial > 0) ((current - initial) * 100 / initial) else 0

        return CargaInfo(
            exerciseName  = exercise?.name         ?: "—",
            currentLoad   = current,
            unit          = exercise?.unit         ?: "kg",
            changePercent = change,
            initialLoad   = initial,
            prLoad        = pr,
            muscleGroup   = exercise?.muscleGroup  ?: "—",
            chartPoints   = points.ifEmpty { listOf(0f, 0f) },
        )
    }

    private fun buildVolumeInfo(domain: VolumeData): VolumeInfo = VolumeInfo(
        totalTons = domain.totalTons,
        sessions = domain.sessions,
        grid = domain.weeklyGrid,
        rowLabels = domain.rowLabels,
    )

    private fun buildCaloriasInfo(domain: CaloriasData): CaloriasInfo = CaloriasInfo(
        avgDaily      = domain.avgDaily,
        changePercent = domain.changePercent,
        today         = domain.today,
        goal          = domain.goal,
        streak        = domain.streak,
        barValues     = domain.history.ifEmpty { listOf(0f, 0f) },
    )

    private fun buildCardioInfo(domain: CardioData): CardioInfo = CardioInfo(
        paceMin         = domain.paceMin,
        paceSec         = domain.paceSec,
        changePercent   = domain.changePercent,
        vo2Max          = domain.vo2Max,
        distKm          = domain.distKm,
        aerobicZoneFrac = domain.aerobicZoneFrac.coerceIn(0f, 1f),
        chartPoints     = domain.paceHistory.ifEmpty { listOf(6f, 6f) },
    )

    private fun buildConsistenciaInfo(domain: ConsistenciaData): ConsistenciaInfo = ConsistenciaInfo(
        currentStreak  = domain.currentStreak,
        recordStreak   = domain.recordStreak,
        weekGrid       = domain.weekGrid,
    )

    // ── Dados intermediários (agrupamento de flows secundários) ───────────────

    private data class SecondaryData(
        val volume       : VolumeData?       = null,
        val calorias     : CaloriasData?     = null,
        val cardio       : CardioData?       = null,
        val consistencia : ConsistenciaData? = null,
    )
}