package com.example.presentation.screens.ui.progress

import androidx.compose.runtime.Immutable
import com.example.domain.models.progress.Exercise
import com.example.domain.models.progress.ProgressionInsight
import com.example.domain.models.progress.ProgressionStats
import ir.ehsannarmani.compose_charts.models.Line

/**
 * Estado único e imutável que descreve completamente a ProgressScreen.
 *
 * ## Tipos de domínio utilizados
 * [Exercise], [ProgressionStats] e [ProgressionInsight] vêm de
 * `com.example.domain.progress.model` — a camada de apresentação os consome
 * mas nunca os redefine.
 *
 * ## Tipos de apresentação definidos aqui
 * [PeriodFilter] é um modelo de UI puro (representa o filtro do usuário)
 * e por isso vive na camada de apresentação.
 */
sealed class ProgressUiState {

    /** Carregamento inicial — exibe Shimmer sobre o layout completo. */
    data object Loading : ProgressUiState()

    /**
     * Dados carregados com sucesso.
     *
     * @param splits              Fichas disponíveis (ex.: ["A", "B", "C"]).
     * @param selectedSplit       Ficha atualmente ativa na tab row.
     * @param exercises           Exercícios filtrados pela ficha selecionada.
     * @param selectedExercise    Exercício em análise (null enquanto carrega).
     * @param isExerciseSheetOpen Controla visibilidade do ModalBottomSheet.
     * @param periodFilter        Intervalo de meses selecionado.
     * @param chartLines          Linhas prontas para a biblioteca de gráficos.
     * @param stats               Estatísticas do período (PR, carga atual, delta).
     * @param insight             Insight textual classificado por nível.
     * @param isRefreshing        Pull-to-refresh em andamento (não oculta conteúdo).
     */
    @Immutable
    data class Success(
        val splits: List<String>,
        val selectedSplit: String,
        val exercises: List<Exercise>,
        val selectedExercise: Exercise?,
        val isExerciseSheetOpen: Boolean = false,
        val periodFilter: PeriodFilter,
        val chartLines: List<Line>,
        val stats: ProgressionStats,
        val insight: ProgressionInsight,
        val isRefreshing: Boolean = false,
    ) : ProgressUiState()

    /**
     * Erro recuperável.
     *
     * @param message  Mensagem legível para o usuário.
     * @param canRetry Exibe botão "Tentar novamente" quando verdadeiro.
     */
    data class Error(
        val message: String,
        val canRetry: Boolean = true,
    ) : ProgressUiState()
}

// ─────────────────────────────────────────────────────────────────────────────
// Modelo de UI — PeriodFilter
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Intervalo de meses selecionado pelos filtros da tela.
 * Mantido na camada de apresentação pois representa estado de UI,
 * não uma regra de negócio.
 */
@Immutable
data class PeriodFilter(
    val startMonth: Int = 1,
    val endMonth: Int = 12,
)

// ─────────────────────────────────────────────────────────────────────────────
// Eventos pontuais — entregues via Channel (garantia de entrega única)
// ─────────────────────────────────────────────────────────────────────────────

sealed class ProgressUiEvent {
    data class ShowSnackbar(val message: String) : ProgressUiEvent()
    data object NavigateBack : ProgressUiEvent()
}

// ─────────────────────────────────────────────────────────────────────────────
// Intenções do usuário → ViewModel
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Todas as ações disparáveis a partir da ProgressScreen.
 * A UI nunca conhece o tratamento de cada ação — apenas as delega.
 */
sealed class ProgressIntent {
    data class SelectSplit(val split: String) : ProgressIntent()
    data class SelectExercise(val exercise: Exercise) : ProgressIntent()
    data object OpenExerciseSheet : ProgressIntent()
    data object CloseExerciseSheet : ProgressIntent()
    data class UpdatePeriod(val filter: PeriodFilter) : ProgressIntent()
    data object Refresh : ProgressIntent()
    data object Retry : ProgressIntent()
    data object NavigateBack : ProgressIntent()
}