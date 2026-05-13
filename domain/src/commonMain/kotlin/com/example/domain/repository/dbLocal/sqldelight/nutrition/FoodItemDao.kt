package com.example.domain.repository.dbLocal.sqldelight.nutrition

interface FoodItemDao {
    suspend fun getFoodsByMeal(mealId: String): List<FoodItemRecord>
    suspend fun getFoodById(id: String): FoodItemRecord?
    suspend fun insertFood(food: FoodItemRecord)
    suspend fun updateFood(food: FoodItemRecord)
    suspend fun deleteFood(id: String)
    suspend fun deleteFoodsByMeal(mealId: String)
    suspend fun getMealMacros(mealId: String): MealMacros
}

data class FoodItemRecord(
    val id: String,
    val mealId: String,
    val name: String,
    val portion: Double,
    val unit: String,
    val kcal: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
)

data class MealMacros(
    val totalKcal: Long,
    val totalProtein: Double,
    val totalCarbs: Double,
    val totalFat: Double,
)
