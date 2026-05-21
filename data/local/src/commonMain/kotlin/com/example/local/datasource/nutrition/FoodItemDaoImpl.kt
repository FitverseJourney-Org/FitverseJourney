package org.fitverse.data.local.datasource.nutrition

import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemDao
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemRecord
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealMacros
import com.journey.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class FoodItemDaoImpl(database: AppDatabase) : FoodItemDao {

    private val queries = database.foodItemEntityQueries

    override suspend fun getFoodsByMeal(mealId: String): List<FoodItemRecord> =
        withContext(Dispatchers.IO) {
            queries.selectFoodsByMeal(mealId).executeAsList().map { it.toRecord() }
        }

    override suspend fun getFoodById(id: String): FoodItemRecord? =
        withContext(Dispatchers.IO) {
            queries.selectFoodById(id).executeAsOneOrNull()?.toRecord()
        }

    override suspend fun insertFood(food: FoodItemRecord): Unit = withContext(Dispatchers.IO) {
        queries.insertFood(
            id      = food.id,
            mealId  = food.mealId,
            name    = food.name,
            portion = food.portion,
            unit    = food.unit,
            kcal    = food.kcal.toLong(),
            protein = food.protein,
            carbs   = food.carbs,
            fat     = food.fat,
        )
    }

    override suspend fun updateFood(food: FoodItemRecord): Unit = withContext(Dispatchers.IO) {
        queries.updateFood(
            name    = food.name,
            portion = food.portion,
            unit    = food.unit,
            kcal    = food.kcal.toLong(),
            protein = food.protein,
            carbs   = food.carbs,
            fat     = food.fat,
            id      = food.id,
        )
    }

    override suspend fun deleteFood(id: String): Unit =
        withContext(Dispatchers.IO) { queries.deleteFood(id) }

    override suspend fun deleteFoodsByMeal(mealId: String): Unit =
        withContext(Dispatchers.IO) { queries.deleteFoodsByMeal(mealId) }

    override suspend fun deleteFoodsByUserBeforeDate(userId: String, beforeDate: String): Unit =
        withContext(Dispatchers.IO) { queries.deleteFoodsByUserBeforeDate(userId = userId, date = beforeDate) }

    override suspend fun getMealMacros(mealId: String): MealMacros =
        withContext(Dispatchers.IO) {
            val row = queries.sumMacrosByMeal(mealId).executeAsOne()
            MealMacros(
                totalKcal    = row.totalKcal,
                totalProtein = row.totalProtein,
                totalCarbs   = row.totalCarbs,
                totalFat     = row.totalFat,
            )
        }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun com.journey.nutrition.FoodItemEntity.toRecord() = FoodItemRecord(
        id      = id,
        mealId  = mealId,
        name    = name,
        portion = portion,
        unit    = unit,
        kcal    = kcal.toInt(),
        protein = protein,
        carbs   = carbs,
        fat     = fat,
    )
}
