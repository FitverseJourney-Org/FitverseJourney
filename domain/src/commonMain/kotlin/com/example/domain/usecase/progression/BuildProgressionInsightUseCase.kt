package com.example.domain.usecase.progression

import com.example.domain.model.progress.InsightLevel
import com.example.domain.model.progress.LoadProgressionPoint
import com.example.domain.model.progress.ProgressionInsight

/**
 * Gera um [ProgressionInsight] com base na variação percentual de carga.
 *
 * Centralizado em um UseCase para que a regra de negócio ("acima de 8% é excelente")
 * possa ser alterada sem tocar na ViewModel ou na UI, e seja testável isoladamente.
 *
 * Limiares (baseados em literatura de periodização):
 * - ≥ 8% → [InsightLevel.EXCELLENT]
 * - ≥ 2% → [InsightLevel.GOOD]
 * - < 2% → [InsightLevel.NEUTRAL]
 * - < 2 pontos → [InsightLevel.NO_DATA]
 */
class BuildProgressionInsightUseCase {

    operator fun invoke(data: List<LoadProgressionPoint>): ProgressionInsight {
        if (data.size < 2) {
            return ProgressionInsight(
                message = "Registre mais sessões para receber insights personalizados.",
                level = InsightLevel.NO_DATA,
            )
        }
        val start = data.first().weight
        val end = data.last().weight
        val changePercent = if (start > 0.0) ((end - start) / start) * 100.0 else 0.0

        return when {
            changePercent >= 8.0 -> ProgressionInsight(
                message = "Excelente! ${changePercent.formatPercent()} de ganho — " +
                        "mantenha o volume e considere aumentar a carga gradualmente.",
                level = InsightLevel.EXCELLENT,
            )
            changePercent >= 2.0 -> ProgressionInsight(
                message = "Bom progresso: ${changePercent.formatPercent()} — reavalie " +
                        "a variação de repetições para manter a curva ascendente.",
                level = InsightLevel.GOOD,
            )
            else -> ProgressionInsight(
                message = "Progresso de ${changePercent.formatPercent()} — verifique " +
                        "recuperação, frequência e variação de estímulo.",
                level = InsightLevel.NEUTRAL,
            )
        }
    }
}