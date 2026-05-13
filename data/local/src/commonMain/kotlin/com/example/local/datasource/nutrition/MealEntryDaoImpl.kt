package com.example.local.datasource.nutrition

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.domain.repository.dbLocal.sqldelight.nutrition.DailyMacros
import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao
import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryRecord
import com.journey.database.AppDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MealEntryDaoImpl(database: AppDatabase) : MealEntryDao {

    private val queries = database.appDatabaseQueries

    override fun observeMealsByDate(userId: String, date: String): Flow<List<MealEntryRecord>> =
        queries.selectMealsByUserAndDate(userId, date)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toRecord() } }

    override suspend fun getMealsByDate(userId: String, date: String): List<MealEntryRecord> =
        withContext(Dispatchers.IO) {
            queries.selectMealsByUserAndDate(userId, date).executeAsList().map { it.toRecord() }
        }

    override suspend fun getMealsByPeriod(userId: String, from: String, to: String): List<MealEntryRecord> =
        withContext(Dispatchers.IO) {
            queries.selectMealsByUserAndPeriod(userId, from, to).executeAsList().map { it.toRecord() }
        }

    override suspend fun getMealById(id: String): MealEntryRecord? =
        withContext(Dispatchers.IO) {
            queries.selectMealById(id).executeAsOneOrNull()?.toRecord()
        }

    override suspend fun insertMeal(meal: MealEntryRecord): Unit = withContext(Dispatchers.IO) {
        queries.insertMeal(
            id           = meal.id,
            userId       = meal.userId,
            date         = meal.date,
            mealType     = meal.mealType,
            name         = meal.name,
            totalKcal    = meal.totalKcal.toLong(),
            totalProtein = meal.totalProtein,
            totalCarbs   = meal.totalCarbs,
            totalFat     = meal.totalFat,
            loggedAt     = meal.loggedAt,
        )
    }

    override suspend fun updateMealTotals(
        id: String, userId: String, kcal: Int, protein: Double, carbs: Double, fat: Double,
    ): Unit = withContext(Dispatchers.IO) {
        queries.updateMealTotals(
            totalKcal    = kcal.toLong(),
            totalProtein = protein,
            totalCarbs   = carbs,
            totalFat     = fat,
            id           = id,
            userId       = userId,
        )
    }

    override suspend fun deleteMeal(id: String, userId: String): Unit =
        withContext(Dispatchers.IO) { queries.deleteMeal(id = id, userId = userId) }

    override suspend fun getDailyMacros(userId: String, date: String): DailyMacros =
        withContext(Dispatchers.IO) {
            val row = queries.sumMacrosByUserAndDate(userId, date).executeAsOne()
            DailyMacros(
                kcal    = row.kcal,
                protein = row.protein,
                carbs   = row.carbs,
                fat     = row.fat,
            )
        }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun com.journey.database.migrations.MealEntryEntity.toRecord() = MealEntryRecord(
        id           = id,
        userId       = userId,
        date         = date,
        mealType     = mealType,
        name         = name,
        totalKcal    = totalKcal.toInt(),
        totalProtein = totalProtein,
        totalCarbs   = totalCarbs,
        totalFat     = totalFat,
        loggedAt     = loggedAt,
    )
}
