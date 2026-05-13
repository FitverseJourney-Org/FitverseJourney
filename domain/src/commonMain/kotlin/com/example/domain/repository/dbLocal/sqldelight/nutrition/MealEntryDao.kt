package com.example.domain.repository.dbLocal.sqldelight.nutrition

import kotlinx.coroutines.flow.Flow

interface MealEntryDao {
    fun observeMealsByDate(userId: String, date: String): Flow<List<MealEntryRecord>>
    suspend fun getMealsByDate(userId: String, date: String): List<MealEntryRecord>
    suspend fun getMealsByPeriod(userId: String, from: String, to: String): List<MealEntryRecord>
    suspend fun getMealById(id: String): MealEntryRecord?
    suspend fun insertMeal(meal: MealEntryRecord)
    suspend fun updateMealTotals(id: String, userId: String, kcal: Int, protein: Double, carbs: Double, fat: Double)
    suspend fun deleteMeal(id: String, userId: String)
    suspend fun getDailyMacros(userId: String, date: String): DailyMacros
}

data class MealEntryRecord(
    val id: String,
    val userId: String,
    val date: String,
    val mealType: String,
    val name: String,
    val totalKcal: Int,
    val totalProtein: Double,
    val totalCarbs: Double,
    val totalFat: Double,
    val loggedAt: Long,
)

data class DailyMacros(
    val kcal: Long,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
)
