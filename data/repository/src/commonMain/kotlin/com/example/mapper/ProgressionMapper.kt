package org.fitverse.data.repository.mapper

import org.fitverse.domain.expect.toPlatformDate
import org.fitverse.domain.models.progression.LoadProgressionPoint
import org.fitverse.data.remote.dto.progression.ProgressionPointDto

/**
 * Funções de extensão para conversão [ProgressionPointDto] ↔ [LoadProgressionPoint].
 *
 * O campo [ProgressionPointDto.epochMillis] (Long) é convertido para
 * [com.example.domain.expect.PlatformDate] via `Long.toPlatformDate()` — função
 * `expect` com implementações nativas por plataforma (java.time / NSDate).
 *
 * A lista de DTOs é ordenada por [epochMillis] no mapper para garantir
 * a pré-condição de ordenação exigida pelos UseCases.
 */

fun ProgressionPointDto.toDomain(): LoadProgressionPoint = LoadProgressionPoint(
    date = epochMillis.toPlatformDate(),
    weight = weightKg,
    estimatedOneRm = estimatedOneRm,
)

fun List<ProgressionPointDto>.toDomain(): List<LoadProgressionPoint> =
    sortedBy(ProgressionPointDto::epochMillis).map { it.toDomain() }

/**
 * Converte [LoadProgressionPoint] para [ProgressionPointDto] para persistência.
 *
 * @param id         ID gerado localmente (UUID) antes de confirmar com o servidor.
 * @param exerciseId FK do exercício ao qual o ponto pertence.
 */
fun LoadProgressionPoint.toDto(id: String, exerciseId: String): ProgressionPointDto =
    ProgressionPointDto(
        id             = id,
        exerciseId     = exerciseId,
        epochMillis    = date.epochMillis,
        weightKg       = weight,
        estimatedOneRm = estimatedOneRm,
    )
