package org.fitverse.presentation.ui.progress

import androidx.compose.runtime.Immutable
import org.fitverse.domain.models.progression.Exercise

// ═════════════════════════════════════════════════════════════════════════════
// PeriodFilter  ─  chips: 7D / 30D / 3M / TUDO
// ═════════════════════════════════════════════════════════════════════════════

enum class PeriodFilter(
    val label : String,
    val days  : Int,
) {
    SEVEN_DAYS   ("7D",   7),
    THIRTY_DAYS  ("30D",  30),
    THREE_MONTHS ("3M",   90),
    ALL          ("TUDO", 365);

    companion object {
        val default = THIRTY_DAYS
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// Gamification track models  ─  compartilhados por 4 cards
// ═════════════════════════════════════════════════════════════════════════════

/**
 * Um marco individual na trilha de progressão gamificada.
 *
 * @param label    Valor exibido abaixo do nó (ex.: "10t", "7d", "25km").
 * @param xpLabel  Recompensa exibida abaixo do label (ex.: "+200", "+1.2k").
 * @param reached  Se este marco já foi desbloqueado.
 */
@Immutable
data class MarcoMilestone(
    val label   : String,
    val xpLabel : String,
    val reached : Boolean,
)

// ═════════════════════════════════════════════════════════════════════════════
// Card UI models
// ═════════════════════════════════════════════════════════════════════════════

/** Card Rank  ─  arco de progresso animado + stats de XP. */
@Immutable
data class RankInfo(
    val currentRank  : String = "PRATA",
    val currentXp    : Int    = 8_548,
    val nextRank     : String = "OURO",
    val xpToNext     : Int    = 6_468,
    /** Fração [0, 1] para o arco e a barra linear animada. */
    val progressFrac : Float  = 0.57f,
)

/**
 * Card Carga  ─  lime green  ─  LineChart + seletor de exercício.
 *
 * @param chartPoints Histórico de carga em kg. Mínimo 2 pontos.
 */
@Immutable
data class CargaInfo(
    val exerciseName  : String      = "Levantamento Terra",
    val currentLoad   : Int         = 145,
    val unit          : String      = "kg",
    /** Variação percentual vs. início do período. Negativo = regressão. */
    val changePercent : Int         = 18,
    val initialLoad   : Int         = 123,
    val prLoad        : Int         = 145,
    val muscleGroup   : String      = "COSTAS",
    val chartPoints   : List<Float> = listOf(
        123f,125f,126f,128f,130f,131f,133f,135f,138f,140f,143f,145f,
    ),
)

/**
 * Card Volume  ─  indigo  ─  heatmap canvas + MarcoInfo.
 *
 * Ordem dos campos alinhada com o MockData para evitar erros de tipo no compilador.
 *
 * @param xpEarned  XP acumulado no período — exibido no badge do header.
 * @param grid      Intensidade [0, 1] por célula (7 linhas × 5 colunas). 0 = sem treino.
 * @param rowLabels Eixo Y: abreviações dos dias da semana — 7 itens.
 * @param marco     Trilha de marcos. `null` oculta o sub-card.
 */
@Immutable
data class VolumeInfo(
    val totalTons  : Float             = 86.4f,
    val sessions   : Int               = 30,
    val xpEarned   : Int               = 2_400,
    val grid       : List<List<Float>> = List(7) { r ->
        List(5) { c -> ((r * 3 + c * 2) % 11 / 10f).coerceIn(0f, 1f) }
    },
    val rowLabels  : List<String>      = listOf("S","T","Q","Q","S","S","D"),
    val marco      : MarcoInfo?        = null,
)

/**
 * Card Calorias  ─  orange  ─  ColumnChart pareado (real + meta) + MarcoInfo.
 *
 * Ordem dos campos alinhada com o MockData para evitar erros de tipo no compilador.
 *
 * @param xpEarned   XP acumulado no período — exibido no badge do header.
 * @param barValues  Histórico diário de calorias ingeridas (mínimo 2 valores).
 * @param diasNaMeta Trilha de marcos "dias na meta". `null` oculta o sub-card.
 */
@Immutable
data class CaloriasInfo(
    val avgDaily      : Int         = 2_182,
    /** Variação percentual vs. período anterior. Positivo = aumento de consumo. */
    val changePercent : Int         = 1,
    val today         : Int         = 2_473,
    val goal          : Int         = 2_200,
    val streak        : Int         = 0,
    val xpEarned      : Int         = 1_820,
    val barValues     : List<Float> = listOf(
        2050f,2350f,1980f,2420f,2180f,2090f,
        2300f,2473f,2150f,2400f,2250f,2182f,
    ),
    val diasNaMeta    : MarcoInfo?  = null,
)

/**
 * Card Cardio  ─  cyan  ─  LineChart (pace invertido) + zonas FC + MarcoInfo.
 *
 * Ordem dos campos alinhada com o MockData para evitar erros de tipo no compilador.
 *
 * @param chartPoints        Histórico de pace em min/km. Valores menores = mais rápido.
 *                           A inversão visual é feita na UI via (maxPace + minPace - v).
 * @param aerobicZoneFrac    Fração [0, 1] da zona aeróbica na barra de FC.
 * @param xpEarned           XP acumulado no período — exibido no badge do header.
 * @param distanciaAcumulada Trilha de marcos de distância. `null` oculta o sub-card.
 */
@Immutable
data class CardioInfo(
    val paceMin            : Int         = 5,
    val paceSec            : Int         = 22,
    /** Melhora de pace vs. período anterior. Positivo = ficou mais rápido. */
    val changePercent      : Int         = 9,
    val vo2Max             : Int         = 48,
    val distKm             : Float       = 42.0f,
    val aerobicZoneFrac    : Float       = 0.70f,
    val xpEarned           : Int         = 950,
    val chartPoints        : List<Float> = listOf(
        6.10f,6.00f,5.90f,5.82f,5.75f,5.68f,5.60f,5.52f,5.40f,5.30f,5.25f,5.22f,
    ),
    val distanciaAcumulada : MarcoInfo?  = null,
)

/**
 * Card Consistência  ─  amber  ─  grid canvas + streak + MarcoInfo.
 *
 * Ordem dos campos alinhada com o MockData para evitar erros de tipo no compilador.
 *
 * @param weekGrid     5 linhas × 7 colunas (domingo → sábado). true = treinou.
 * @param xpEarned     XP acumulado no período — exibido no badge do header.
 * @param marcosStreak Trilha de marcos de streak. `null` oculta o sub-card.
 */
@Immutable
data class ConsistenciaInfo(
    val currentStreak : Int                 = 23,
    val recordStreak  : Int                 = 47,
    val xpEarned      : Int                 = 3_200,
    val weekGrid      : List<List<Boolean>> = listOf(
        listOf(false,false,true, true, true, true, true),
        listOf(true, true, true, true, true, true, true),
        listOf(true, true, true, true, true, true, true),
        listOf(true, true, true, true, true, false,false),
        listOf(true, true, true, true, false,false,false),
    ),
    val marcosStreak  : MarcoInfo?          = null,
)

// ═════════════════════════════════════════════════════════════════════════════
// ProgressUiState  ─  estado único e imutável da tela
// ═════════════════════════════════════════════════════════════════════════════

sealed class ProgressUiState {

    /** Carregamento inicial — exibe skeleton animado. */
    data object Loading : ProgressUiState()

    /**
     * Dados carregados com sucesso.
     *
     * Cada propriedade mapeia diretamente para um item da LazyColumn.
     * O Compose só recompõe o card cujo data class mudou.
     *
     * @param period       Filtro de período ativo (chip selecionado).
     * @param rank         Dados do card de rank/XP.
     * @param carga        Dados de carga + exercício selecionado.
     * @param exercises    Lista de exercícios da ficha ativa (para o seletor/sheet).
     * @param volume       Dados do heatmap de volume.
     * @param calorias     Dados do bar-chart de calorias.
     * @param cardio       Dados do line-chart de cardio.
     * @param consistencia Dados do grid de consistência.
     * @param isRefreshing Pull-to-refresh em andamento — não oculta conteúdo.
     */
    @Immutable
    data class Success(
        val period        : PeriodFilter     = PeriodFilter.default,
        val rank          : RankInfo         = RankInfo(),
        val carga         : CargaInfo        = CargaInfo(),
        val exercises     : List<Exercise>   = emptyList(),
        val volume        : VolumeInfo       = VolumeInfo(),
        val calorias      : CaloriasInfo     = CaloriasInfo(),
        val cardio        : CardioInfo       = CardioInfo(),
        val consistencia  : ConsistenciaInfo = ConsistenciaInfo(),
        val isRefreshing  : Boolean          = false,
    ) : ProgressUiState()

    /**
     * Falha recuperável ou não.
     *
     * @param message  Texto amigável exibido na tela de erro.
     * @param canRetry Exibe o botão "Tentar novamente" quando `true`.
     */
    data class Error(
        val message  : String,
        val canRetry : Boolean = true,
    ) : ProgressUiState()
}

// ═════════════════════════════════════════════════════════════════════════════
// ProgressIntent  ─  ações UI → ViewModel
// ═════════════════════════════════════════════════════════════════════════════

/**
 * Todas as ações da ProgressionScreen.
 * O `when` exaustivo no ViewModel garante que novos intents
 * quebrem a compilação se não forem tratados.
 */
sealed class ProgressIntent {
    data class  SelectPeriod(val period: PeriodFilter) : ProgressIntent()
    data class  SelectExercise(val exercise: Exercise) : ProgressIntent()
    data object OpenExerciseSheet                      : ProgressIntent()
    data object CloseExerciseSheet                     : ProgressIntent()
    data object Refresh                                : ProgressIntent()
    data object Retry                                  : ProgressIntent()
    data object NavigateBack                           : ProgressIntent()
}

// ═════════════════════════════════════════════════════════════════════════════
// ProgressUiEvent  ─  efeitos colaterais entregues via Channel
// ═════════════════════════════════════════════════════════════════════════════

sealed class ProgressUiEvent {
    data object NavigateBack                       : ProgressUiEvent()
    data class  ShowSnackbar(val message: String)  : ProgressUiEvent()
}