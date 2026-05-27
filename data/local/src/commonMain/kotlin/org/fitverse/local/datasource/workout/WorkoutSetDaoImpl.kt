package org.fitverse.data.local.datasource.workout

import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSetDao
import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSetRecord
import com.journey.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class WorkoutSetDaoImpl(database: AppDatabase) : WorkoutSetDao {

    private val queries = database.workoutSetEntityQueries

    override suspend fun getSetsBySession(sessionId: String): List<WorkoutSetRecord> =
        withContext(Dispatchers.IO) {
            queries.selectSetsBySession(sessionId).executeAsList().map { it.toRecord() }
        }

    override suspend fun getSetsByExercise(exerciseName: String): List<WorkoutSetRecord> =
        withContext(Dispatchers.IO) {
            queries.selectSetsByExercise(exerciseName).executeAsList().map { it.toRecord() }
        }

    override suspend fun getPRByExercise(exerciseName: String): WorkoutSetRecord? =
        withContext(Dispatchers.IO) {
            queries.selectPRsByExercise(exerciseName).executeAsOneOrNull()?.toRecord()
        }

    override suspend fun getMaxWeightForExercise(exerciseName: String): Double =
        withContext(Dispatchers.IO) {
            // Executa a query e pega apenas o primeiro resultado (scalar)
            queries.selectMaxWeightByExercise(exerciseName)
                .executeAsOneOrNull()?.MAX ?: 0.0 // Se for null, retorna 0.0
        }

    override suspend fun insertSet(set: WorkoutSetRecord): Unit = withContext(Dispatchers.IO) {
        queries.insertSet(
            id           = set.id,
            sessionId    = set.sessionId,
            exerciseName = set.exerciseName,
            muscleGroup  = set.muscleGroup,
            setNumber    = set.setNumber.toLong(),
            reps         = set.reps.toLong(),
            weight       = set.weight,
            isPR         = if (set.isPR) 1L else 0L,
            rpe          = set.rpe?.toLong(),
            notes        = set.notes,
        )
    }

    override suspend fun insertSets(sets: List<WorkoutSetRecord>): Unit =
        withContext(Dispatchers.IO) { sets.forEach { insertSet(it) } }

    override suspend fun deleteSetsBySession(sessionId: String): Unit =
        withContext(Dispatchers.IO) { queries.deleteSetsBySession(sessionId) }

    override suspend fun deleteSet(id: String): Unit =
        withContext(Dispatchers.IO) { queries.deleteSet(id) }

    override suspend fun countPRsInSession(sessionId: String): Long =
        withContext(Dispatchers.IO) { queries.countPRsInSession(sessionId).executeAsOne() }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun com.journey.workout.WorkoutSetEntity.toRecord() = WorkoutSetRecord(
        id           = id,
        sessionId    = sessionId,
        exerciseName = exerciseName,
        muscleGroup  = muscleGroup,
        setNumber    = setNumber.toInt(),
        reps         = reps.toInt(),
        weight       = weight,
        isPR         = isPR != 0L,
        rpe          = rpe?.toInt(),
        notes        = notes,
    )
}
