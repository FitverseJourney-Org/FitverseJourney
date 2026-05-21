package org.fitverse.domain.usecase.progression

import org.fitverse.domain.models.progression.Exercise
import org.fitverse.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlin.collections.emptyList

/**
 * Retorna os exercícios de uma ficha específica, ordenados por nome.
 *
 * O parâmetro [trainingSplit] é validado — uma string vazia emite lista vazia
 * imediatamente, sem chamar o repositório.
 *
 * @param trainingSplit Identificador da ficha (ex.: "A", "Empurrar").
 */
class GetExercisesByTrainingSplitUseCase(
    private val exerciseRepository: ExerciseRepository,
) {
    operator fun invoke(trainingSplit: String): Flow<List<Exercise>> {
        if (trainingSplit.isBlank()) return kotlinx.coroutines.flow.flowOf(emptyList())
        return exerciseRepository
            .getExercisesByTrainingSplit(trainingSplit)
            .map { exercises -> exercises.sortedBy { it.name } }
            .catch { emit(emptyList()) }
    }
}