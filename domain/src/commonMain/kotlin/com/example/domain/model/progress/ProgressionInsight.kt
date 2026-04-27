package com.example.domain.model.progress

import androidx.compose.runtime.Immutable

/**
 * Insight gerado por [BuildProgressionInsightUseCase].
 *
 * Pertence ao **domínio** porque a lógica de classificação ("bom/excelente")
 * é uma regra de negócio, não uma decisão de UI.
 * A camada de apresentação mapeia [InsightLevel] → cor/ícone sem condicional.
 */
@Immutable
data class ProgressionInsight(
    val message: String,
    val level: InsightLevel,
)

/**
 * Classificação de nível de progressão.
 * Permite que a UI adapte cores e ícones sem nenhuma lógica `if/when` local.
 */
enum class InsightLevel {
    EXCELLENT,  // ≥ 8% de ganho no período
    GOOD,       // ≥ 2% de ganho no período
    NEUTRAL,    // < 2% de ganho no período
    NO_DATA,    // Dados insuficientes (< 2 sessões)
}