package com.example.domain.usecase.progression

import com.example.domain.models.progress.ProgressionData
import com.example.domain.repository.ProgressionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * Orquestra a busca de dados de progressão para o período selecionado,
 * combinando o período atual com o comparativo do ano anterior.
 *
 * ## Lógica de negócio encapsulada
 * - Filtra os pontos retornados pelo repositório pelo intervalo [startMonth]..[endMonth].
 * - Busca simultaneamente os pontos do mesmo período no ano anterior (linha comparativa).
 * - Combina ambos em um único [ProgressionData] para entrega atômica à ViewModel.
 *
 * Usar [combine] garante que o estado nunca fique parcialmente atualizado:
 * a UI só recebe o novo estado quando **ambas** as listas estiverem prontas.
 *
 * @param exerciseId ID do exercício selecionado.
 * @param startMonth Primeiro mês do período (1–12).
 * @param endMonth   Último mês do período (1–12, deve ser ≥ startMonth).
 * @param currentYear Ano atual para calcular o ano comparativo.
 */
class GetProgressionDataUseCase(
    private val progressionRepository: ProgressionRepository,
) {
    operator fun invoke(
        exerciseId: String,
        startMonth: Int,
        endMonth: Int,
        currentYear: Int,
    ): Flow<Result<ProgressionData>> {
        val currentFlow = progressionRepository.getProgressionPoints(exerciseId).map { points ->
            points.filter {
                it.date.month in startMonth..endMonth
            }
        }


        val previousFlow = progressionRepository.getPreviousPeriodPoints(
            exerciseId = exerciseId,
            startMonth = startMonth,
            endMonth = endMonth,
            year = currentYear,
        )


        return combine(currentFlow, previousFlow) { current, previous ->
            Result.success(ProgressionData(current = current, previous = previous))
        }.catch { throwable ->
            emit(Result.failure(throwable))
        }
    }
}