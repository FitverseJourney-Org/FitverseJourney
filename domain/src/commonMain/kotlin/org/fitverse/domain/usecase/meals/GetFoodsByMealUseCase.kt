package org.fitverse.domain.usecase.meals

import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemDao
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemRecord

class GetFoodsByMealUseCase(
    private val foodDao: FoodItemDao,
) {
    suspend operator fun invoke(mealId: String): List<FoodItemRecord> =
        foodDao.getFoodsByMeal(mealId)
}
