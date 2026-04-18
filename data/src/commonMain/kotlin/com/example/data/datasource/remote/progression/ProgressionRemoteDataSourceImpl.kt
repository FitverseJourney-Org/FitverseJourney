package com.example.data.datasource.remote.progression

import com.example.data.model.dto.progression.ProgressionPointDto
import com.example.expect.DateTimeManager
import kotlin.random.Random

// ─────────────────────────────────────────────────────────────────────────────
// Implementação fake (desenvolvimento / testes)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Implementação fake do [ProgressionRemoteDataSource].
 *
 * Gera dados de progressão sintéticos com progressão realista (ruído gaussiano
 * em torno de uma tendência crescente) para desenvolvimento sem backend.
 *
 * Em produção, substitua por `ProgressionKtorDataSource` no módulo de DI.
 *
 * @param networkDelayMs Delay simulado de rede em ms (padrão: 600ms).
 */
class ProgressionRemoteDataSourceImpl(
    private val networkDelayMs: Long = 600L,
) : ProgressionRemoteDataSource {

    override suspend fun fetchProgressionPoints(
        exerciseId: String,
    ): Result<List<ProgressionPointDto>> = runCatching {
        kotlinx.coroutines.delay(networkDelayMs)
        generateFakePoints(exerciseId, count = 12)
    }

    override suspend fun fetchProgressionPointsByPeriod(
        exerciseId: String,
        startEpoch: Long,
        endEpoch: Long,
    ): Result<List<ProgressionPointDto>> = runCatching {
        kotlinx.coroutines.delay(networkDelayMs / 2)
        generateFakePoints(exerciseId, count = 6)
    }

    override suspend fun postProgressionPoint(
        point: ProgressionPointDto,
    ): Result<ProgressionPointDto> = runCatching {
        kotlinx.coroutines.delay(300L)
        // Simula o servidor retornando o mesmo DTO com um ID confirmado
        point.copy(id = "srv_${point.id}")
    }

    // ── Gerador de dados sintéticos ────────────────────────────────────────

    private fun generateFakePoints(
        exerciseId: String,
        count: Int,
        baseWeight: Double = 70.0,
    ): List<ProgressionPointDto> {
        val now = DateTimeManager.now()
        val weekMillis = 7L * 24 * 60 * 60 * 1000

        return (0 until count).map { index ->
            val noise = Random.nextDouble(from = -1.0, until = 3.0) // -1 a +3 kg
            val trend = index * 0.8                       // 0.8 kg/semana
            val weight = (baseWeight + trend + noise).coerceAtLeast(baseWeight * 0.9)
            val reps = (6..10).random()

            ProgressionPointDto(
                id = "${exerciseId}_point_$index",
                exerciseId = exerciseId,
                epochMillis = now - (count - index) * weekMillis,
                weightKg = (weight * 2).toLong().toDouble() / 2.0, // arredonda em 0.5
                estimatedOneRm = weight * (1 + reps / 30.0),
                reps = reps,
                sets = 3,
            )
        }
    }
}