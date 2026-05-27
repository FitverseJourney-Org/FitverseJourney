package org.fitverse.domain.usecase.meals

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemDao
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemRecord
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao

class AddFoodToMealUseCase(
    private val foodDao: FoodItemDao,
    private val mealDao: MealEntryDao,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(food: FoodItemRecord): Result<Unit> = runCatching {
        val userId = authRepository.getCurrentUserId() ?: error("Not authenticated")
        foodDao.insertFood(food)
        val macros = foodDao.getMealMacros(food.mealId)
        mealDao.updateMealTotals(
            id      = food.mealId,
            userId  = userId,
            kcal    = macros.totalKcal.toInt(),
            protein = macros.totalProtein,
            carbs   = macros.totalCarbs,
            fat     = macros.totalFat,
        )
    }
}
