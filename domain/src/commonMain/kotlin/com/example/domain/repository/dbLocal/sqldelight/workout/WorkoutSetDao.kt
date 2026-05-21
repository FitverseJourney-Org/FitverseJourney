package org.fitverse.domain.repository.dbLocal.sqldelight.workout

interface WorkoutSetDao {
    suspend fun getSetsBySession(sessionId: String): List<WorkoutSetRecord>
    suspend fun getSetsByExercise(exerciseName: String): List<WorkoutSetRecord>
    suspend fun getPRByExercise(exerciseName: String): WorkoutSetRecord?
    suspend fun getMaxWeightForExercise(exerciseName: String): Double?
    suspend fun insertSet(set: WorkoutSetRecord)
    suspend fun insertSets(sets: List<WorkoutSetRecord>)
    suspend fun deleteSetsBySession(sessionId: String)
    suspend fun deleteSet(id: String)
    suspend fun countPRsInSession(sessionId: String): Long
}

data class WorkoutSetRecord(
    val id: String,
    val sessionId: String,
    val exerciseName: String,
    val muscleGroup: String,
    val setNumber: Int,
    val reps: Int,
    val weight: Double,
    val isPR: Boolean,
    val rpe: Int?,
    val notes: String,
)
