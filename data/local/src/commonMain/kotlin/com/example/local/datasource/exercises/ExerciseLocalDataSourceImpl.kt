package com.example.local.datasource.exercises

import com.example.local.model.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ExerciseLocalDataSourceImpl : ExerciseLocalDataSource {

    private val _store = MutableStateFlow(SEED_EXERCISES)

    override fun observeAll(): Flow<List<ExerciseEntity>> =
        _store.map { it.filter(ExerciseEntity::isActive) }

    override fun observeByTrainingSplit(trainingSplit: String): Flow<List<ExerciseEntity>> =
        _store.map { exercises ->
            exercises.filter { it.isActive && it.trainingSplit == trainingSplit }
        }

    override fun observeTrainingSplits(): Flow<List<String>> =
        _store.map { exercises ->
            exercises
                .filter(ExerciseEntity::isActive)
                .map(ExerciseEntity::trainingSplit)
                .distinct()
                .sorted()
        }

    override suspend fun findById(id: String): ExerciseEntity? =
        _store.value.find { it.id == id }

    override suspend fun upsertAll(exercises: List<ExerciseEntity>) {
        val current = _store.value.associateBy(ExerciseEntity::id).toMutableMap()
        exercises.forEach { current[it.id] = it }
        _store.value = current.values.toList()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Dados seed para desenvolvimento
// ─────────────────────────────────────────────────────────────────────────────

private val SEED_EXERCISES = listOf(
    // Ficha A – Peito / Tríceps
    ExerciseEntity("ex_001", "Supino Reto com Barra", "Peitoral", "A"),
    ExerciseEntity("ex_002", "Supino Inclinado Halteres", "Peitoral",     "A"),
    ExerciseEntity("ex_003", "Crucifixo na Polia",        "Peitoral",     "A"),
    ExerciseEntity("ex_004", "Tríceps Pulley",            "Tríceps",      "A"),
    ExerciseEntity("ex_005", "Tríceps Testa",             "Tríceps",      "A"),
    // Ficha B – Costas / Bíceps
    ExerciseEntity("ex_006", "Puxada Frontal",            "Latíssimo",    "B"),
    ExerciseEntity("ex_007", "Remada Curvada",            "Dorsal",       "B"),
    ExerciseEntity("ex_008", "Remada Unilateral",         "Dorsal",       "B"),
    ExerciseEntity("ex_009", "Rosca Direta com Barra",    "Bíceps",       "B"),
    ExerciseEntity("ex_010", "Rosca Martelo",             "Braquial",     "B"),
    // Ficha C – Pernas
    ExerciseEntity("ex_011", "Agachamento Livre",         "Quadríceps",   "C"),
    ExerciseEntity("ex_012", "Leg Press 45°",             "Quadríceps",   "C"),
    ExerciseEntity("ex_013", "Cadeira Extensora",         "Quadríceps",   "C"),
    ExerciseEntity("ex_014", "Stiff com Halteres",        "Posterior",    "C"),
    ExerciseEntity("ex_015", "Panturrilha em Pé",         "Gastrocnêmio", "C"),
)