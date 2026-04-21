package com.example.local.datasource.progression

import com.example.data.model.dto.progression.ProgressionPointDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

// Epoch millis de 2025-01-01 00:00:00 UTC — usado como base dos dados de seed.
private const val EPOCH_2025_JAN_01: Long = 1_735_689_600_000L

// ─────────────────────────────────────────────────────────────────────────────
// Extração de ano e mês a partir de epoch millis — inlinada no arquivo
// Algoritmo de Howard Hinnant (domínio público). Zero imports de plataforma.
// ─────────────────────────────────────────────────────────────────────────────

/** Extrai o ano civil de um epoch millis UTC. */
private fun Long.toCalendarYear(): Int {
    val z   = this / 86_400_000L + 719_468L
    val era = (if (z >= 0L) z else z - 146_096L) / 146_097L
    val doe = (z - era * 146_097L).toInt()
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    val y   = yoe.toLong() + era * 400L
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp  = (5 * doy + 2) / 153
    val m   = mp + if (mp < 10) 3 else -9
    return (y + if (m <= 2) 1L else 0L).toInt()
}

/** Extrai o mês civil (1-based) de um epoch millis UTC. */
private fun Long.toCalendarMonth(): Int {
    val z   = this / 86_400_000L + 719_468L
    val era = (if (z >= 0L) z else z - 146_096L) / 146_097L
    val doe = (z - era * 146_097L).toInt()
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp  = (5 * doy + 2) / 153
    return mp + if (mp < 10) 3 else -9
}

// ─────────────────────────────────────────────────────────────────────────────
// Interface
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Contrato para a fonte de dados local de pontos de progressão de carga.
 *
 * Em produção: implementado com **SQLDelight** (KMP) ou **Room** (Android).
 * Em testes: [ProgressionLocalDataSourceImpl] com dados em memória.
 *
 * ## Por que não há java.util.Calendar aqui?
 * A extração de ano/mês é feita por [epochYear] e [epochMonth] —
 * funções de aritmética pura em `EpochDateUtils.kt` que funcionam
 * em todos os targets KMP sem nenhuma dependência de plataforma.
 */

// ─────────────────────────────────────────────────────────────────────────────
// Dados seed — declarados ANTES da classe que os consome
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Par (exerciseId, baseWeight) usado apenas para gerar os dados de seed.
 *
 * Declarado no nível do arquivo (não dentro da função) para compatibilidade
 * com todos os targets KMP — `data class` locais dentro de funções não são
 * suportados em alguns compiladores Kotlin/Native.
 */
private data class ExerciseSeed(val exerciseId: String, val baseWeight: Double)

/**
 * Ruído fixo e determinístico aplicado semana a semana.
 * Sem [kotlin.random.Random], sem seed aleatória — garante que os dados
 * de desenvolvimento sejam idênticos em qualquer execução (útil para
 * snapshots de UI e testes de regressão visual).
 */
private val SEED_NOISE_PATTERN = listOf(
    0.0, 0.5, -0.5, 1.0, 0.0, 0.5, -0.25, 0.75, 0.0, 1.25, -0.5, 0.5,
)

/**
 * Exercícios e cargas base para geração dos dados de seed.
 * Cobre um exercício de cada ficha (A, B, C) para popular o gráfico
 * assim que o app abre, sem precisar de backend.
 */
private val EXERCISE_SEEDS = listOf(
    ExerciseSeed(exerciseId = "ex_001", baseWeight = 80.0),   // Supino Reto     (Ficha A)
    ExerciseSeed(exerciseId = "ex_004", baseWeight = 40.0),   // Tríceps Pulley  (Ficha A)
    ExerciseSeed(exerciseId = "ex_006", baseWeight = 60.0),   // Puxada Frontal  (Ficha B)
    ExerciseSeed(exerciseId = "ex_011", baseWeight = 100.0),  // Agachamento     (Ficha C)
)

/**
 * Gera 12 semanas de progressão realista para [EXERCISE_SEEDS].
 *
 * Todos os timestamps são construídos com a constante [EPOCH_2025_JAN_01] —
 * zero dependências de plataforma. Cada ponto avança 0.75 kg/semana sobre
 * a base, com o ruído de [SEED_NOISE_PATTERN] aplicado por índice.
 *
 * Deve ser chamada **antes** do `init` da classe que a consome — por isso
 * está declarada acima de [ProgressionLocalDataSourceImpl].
 */
private fun seedProgressionPoints(): List<ProgressionPointDto> {
    val baseEpoch  = EPOCH_2025_JAN_01
    val weekMillis = 7L * 24L * 60L * 60L * 1_000L
    val reps       = 8

    return buildList {
        EXERCISE_SEEDS.forEach { seed ->
            (0 until 12).forEach { weekIndex ->
                val noise  = SEED_NOISE_PATTERN[weekIndex % SEED_NOISE_PATTERN.size]
                val weight = seed.baseWeight + (weekIndex * 0.75) + noise

                add(
                    ProgressionPointDto(
                        id             = "${seed.exerciseId}_w$weekIndex",
                        exerciseId     = seed.exerciseId,
                        epochMillis    = baseEpoch + weekIndex * weekMillis,
                        weightKg       = (weight * 2).toLong().toDouble() / 2.0,
                        estimatedOneRm = weight * (1.0 + reps / 30.0),
                        reps           = reps,
                        sets           = 3,
                    )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Implementação em memória (desenvolvimento / testes)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Implementação fake usando [MutableStateFlow] como banco em memória.
 *
 * - Pré-populada com [seedProgressionPoints], declarada **acima** desta classe
 *   para que o compilador KMP resolva a referência sem ambiguidade.
 * - Re-emite automaticamente após qualquer mutação (insert / upsert / delete),
 *   espelhando o comportamento de queries reativas do SQLDelight/Room.
 * - **Zero imports Java** — usa [epochYear] e [epochMonth] de `EpochDateUtils.kt`.
 */
class ProgressionLocalDataSourceImpl : ProgressionLocalDataSource {

    private val _store = MutableStateFlow(seedProgressionPoints())

    override fun observeByExercise(exerciseId: String): Flow<List<ProgressionPointDto>> =
        _store.map { points ->
            points
                .filter { it.exerciseId == exerciseId }
                .sortedBy { it.epochMillis }
        }

    override fun observeByExerciseAndYearPeriod(
        exerciseId: String,
        year: Int,
        startMonth: Int,
        endMonth: Int,
    ): Flow<List<ProgressionPointDto>> =
        _store.map { points ->
            points
                .filter { point ->
                    point.exerciseId == exerciseId &&
                            point.epochMillis.toCalendarYear()  == year &&
                            point.epochMillis.toCalendarMonth() in startMonth..endMonth
                }
                .sortedBy { it.epochMillis }
        }

    override suspend fun insert(point: ProgressionPointDto) {
        _store.value += point
    }

    override suspend fun upsertAll(points: List<ProgressionPointDto>) {
        val current = _store.value.associateBy(ProgressionPointDto::id).toMutableMap()
        points.forEach { current[it.id] = it }
        _store.value = current.values.sortedBy(ProgressionPointDto::epochMillis)
    }

    override suspend fun deleteByExercise(exerciseId: String) {
        _store.value = _store.value.filter { it.exerciseId != exerciseId }
    }
}