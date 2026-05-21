package org.fitverse.presentation.ui.meals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryRecord
import org.fitverse.domain.usecase.meals.CleanupOldMealsUseCase
import org.fitverse.domain.usecase.meals.CreateMealUseCase
import org.fitverse.domain.usecase.meals.DeleteMealUseCase
import org.fitverse.domain.usecase.meals.GetDailyMacrosUseCase
import org.fitverse.domain.usecase.meals.GetFoodsByMealUseCase
import org.fitverse.domain.usecase.meals.InsertMealUseCase
import org.fitverse.domain.usecase.meals.ObserveMealsByDateUseCase
import org.fitverse.presentation.expect.DateFormatter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MealsViewModel(
    private val observeMealsByDate: ObserveMealsByDateUseCase,
    private val createMeal: CreateMealUseCase,
    private val insertMeal: InsertMealUseCase,
    private val deleteMeal: DeleteMealUseCase,
    private val getDailyMacros: GetDailyMacrosUseCase,
    private val getFoodsByMeal: GetFoodsByMealUseCase,
    private val cleanupOldMeals: CleanupOldMealsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MealsUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<MealsEvent>()
    val events = _events.receiveAsFlow()

    private val today: String
        get() = DateFormatter.getTodayIsoDate()

    init {
        viewModelScope.launch { cleanupOldMeals(today) }
        observeToday()
    }

    fun onIntent(intent: MealsIntent) {
        when (intent) {
            is MealsIntent.CreateMeal  -> createNewMeal(intent.name)
            is MealsIntent.AddMeal     -> addMeal(intent.record)
            is MealsIntent.DeleteMeal  -> removeMeal(intent.mealId)
            is MealsIntent.SelectDate  -> changeDate(intent.date)
        }
    }

    private fun observeToday() {
        viewModelScope.launch {
            val date = today
            observeMealsByDate(date)
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { records ->
                    _uiState.update { it.copy(meals = records, selectedDate = date, isLoading = false) }
                    refreshMacros()
                    refreshFoods(records.map { it.id })
                }
        }
    }

    private fun createNewMeal(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val now = DateFormatter.getCurrentTimeMillis()
            createMeal(name = name, date = today, nowMillis = now)
        }
    }

    private fun addMeal(record: MealEntryRecord) {
        viewModelScope.launch { insertMeal(record) }
    }

    private fun removeMeal(mealId: String) {
        viewModelScope.launch { deleteMeal(mealId) }
    }

    private fun changeDate(date: String) {
        _uiState.update { it.copy(selectedDate = date, isLoading = true) }
        viewModelScope.launch {
            observeMealsByDate(date)
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { records ->
                    _uiState.update { it.copy(meals = records, isLoading = false) }
                    refreshMacros()
                    refreshFoods(records.map { it.id })
                }
        }
    }

    private fun refreshMacros() {
        viewModelScope.launch {
            getDailyMacros(_uiState.value.selectedDate)
                .onSuccess { macros ->
                    _uiState.update { it.copy(dailyMacros = macros) }
                }
        }
    }

    private fun refreshFoods(mealIds: List<String>) {
        viewModelScope.launch {
            val map = mealIds.associateWith { id -> getFoodsByMeal(id) }
            _uiState.update { it.copy(foodsByMeal = map) }
        }
    }
}
