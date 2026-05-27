package org.fitverse.domain.usecase.progression

import org.fitverse.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlin.collections.emptyList

/**
 * Retorna a lista de fichas (splits) distintas disponíveis para o usuário.
 *
 * Aplica regras de apresentação puras:
 * - Remove duplicatas (garantido pelo repositório, mas reforçado aqui).
 * - Ordena alfabeticamente.
 * - Emite lista vazia em vez de propagar erro, deixando a UI em estado seguro.
 *
 * Depende apenas de [ExerciseRepository] — testável com um fake simples.
 */
class GetTrainingSplitsUseCase(
    private val exerciseRepository: ExerciseRepository,
) {
    operator fun invoke(): Flow<List<String>> =
        exerciseRepository
            .getTrainingSplits()
            .map { splits -> splits.distinct().sorted() }
            .catch { emit(emptyList()) }
}