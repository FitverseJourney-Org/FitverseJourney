package org.fitverse.domain.models.progression

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
