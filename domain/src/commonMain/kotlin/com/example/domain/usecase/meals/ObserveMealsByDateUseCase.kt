package com.example.domain.usecase.meals

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao
import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class ObserveMealsByDateUseCase(
    private val dao: MealEntryDao,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(date: String): Flow<List<MealEntryRecord>> {
        val userId = authRepository.getCurrentUserId() ?: return emptyFlow()
        return dao.observeMealsByDate(userId, date)
    }
}
