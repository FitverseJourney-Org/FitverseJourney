package com.example.local.datasource.exercises

import com.example.data.model.dto.progression.ExerciseDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

// ─────────────────────────────────────────────────────────────────────────────
// Implementação em memória (desenvolvimento / testes)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Implementação fake usando [MutableStateFlow] como banco em memória.
 *
 * Propósito:
 * 1. Substituto para desenvolvimento sem banco configurado.
 * 2. Implementação injetável em unit tests sem banco real.
 *
 * Em produção, substitua por `ExerciseSqlDelightDataSource` no módulo de DI.
 */
class ExerciseLocalDataSourceImpl : ExerciseLocalDataSource {

    private val _store = MutableStateFlow(SEED_EXERCISES)

    override fun observeAll(): Flow<List<ExerciseDto>> =
        _store.map { it.filter(ExerciseDto::isActive) }

    override fun observeByTrainingSplit(trainingSplit: String): Flow<List<ExerciseDto>> =
        _store.map { exercises ->
            exercises.filter { it.isActive && it.trainingSplit == trainingSplit }
        }

    override fun observeTrainingSplits(): Flow<List<String>> =
        _store.map { exercises ->
            exercises
                .filter(ExerciseDto::isActive)
                .map(ExerciseDto::trainingSplit)
                .distinct()
                .sorted()
        }

    override suspend fun findById(id: String): ExerciseDto? =
        _store.value.find { it.id == id }

    override suspend fun upsertAll(exercises: List<ExerciseDto>) {
        val current = _store.value.associateBy(ExerciseDto::id).toMutableMap()
        exercises.forEach { current[it.id] = it }
        _store.value = current.values.toList()
    }

    override suspend fun softDelete(id: String) {
        _store.value = _store.value.map { ex ->
            if (ex.id == id) ex.copy(isActive = false) else ex
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Dados seed para desenvolvimento
// ─────────────────────────────────────────────────────────────────────────────

private val SEED_EXERCISES = listOf(
    // Ficha A – Peito / Tríceps
    ExerciseDto("ex_001", "Supino Reto com Barra", "Peitoral", "A"),
    ExerciseDto("ex_002", "Supino Inclinado Halteres", "Peitoral",     "A"),
    ExerciseDto("ex_003", "Crucifixo na Polia",        "Peitoral",     "A"),
    ExerciseDto("ex_004", "Tríceps Pulley",            "Tríceps",      "A"),
    ExerciseDto("ex_005", "Tríceps Testa",             "Tríceps",      "A"),
    // Ficha B – Costas / Bíceps
    ExerciseDto("ex_006", "Puxada Frontal",            "Latíssimo",    "B"),
    ExerciseDto("ex_007", "Remada Curvada",            "Dorsal",       "B"),
    ExerciseDto("ex_008", "Remada Unilateral",         "Dorsal",       "B"),
    ExerciseDto("ex_009", "Rosca Direta com Barra",    "Bíceps",       "B"),
    ExerciseDto("ex_010", "Rosca Martelo",             "Braquial",     "B"),
    // Ficha C – Pernas
    ExerciseDto("ex_011", "Agachamento Livre",         "Quadríceps",   "C"),
    ExerciseDto("ex_012", "Leg Press 45°",             "Quadríceps",   "C"),
    ExerciseDto("ex_013", "Cadeira Extensora",         "Quadríceps",   "C"),
    ExerciseDto("ex_014", "Stiff com Halteres",        "Posterior",    "C"),
    ExerciseDto("ex_015", "Panturrilha em Pé",         "Gastrocnêmio", "C"),
)