package com.example.presentation.ui.meals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.dbLocal.sqldelight.nutrition.DailyMacros
import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryRecord
import com.example.domain.usecase.meals.DeleteMealUseCase
import com.example.domain.usecase.meals.GetDailyMacrosUseCase
import com.example.domain.usecase.meals.InsertMealUseCase
import com.example.domain.usecase.meals.ObserveMealsByDateUseCase
import com.example.expect.PlatformDateFormatter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MealsViewModel(
    private val observeMealsByDate: ObserveMealsByDateUseCase,
    private val insertMeal: InsertMealUseCase,
    private val deleteMeal: DeleteMealUseCase,
    private val getDailyMacros: GetDailyMacrosUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MealsUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<MealsEvent>()
    val events = _events.receiveAsFlow()

    private val today: String
        get() = PlatformDateFormatter.getTodayIsoDate()

    init {
        observeToday()
    }

    fun onIntent(intent: MealsIntent) {
        when (intent) {
            is MealsIntent.AddMeal    -> addMeal(intent.record)
            is MealsIntent.DeleteMeal -> removeMeal(intent.mealId)
            is MealsIntent.SelectDate -> changeDate(intent.date)
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
                }
        }
    }

    private fun addMeal(record: MealEntryRecord) {
        viewModelScope.launch {
            insertMeal(record)
        }
    }

    private fun removeMeal(mealId: String) {
        viewModelScope.launch {
            deleteMeal(mealId)
        }
    }

    private fun changeDate(date: String) {
        _uiState.update { it.copy(selectedDate = date, isLoading = true) }
        viewModelScope.launch {
            observeMealsByDate(date)
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { records ->
                    _uiState.update { it.copy(meals = records, isLoading = false) }
                    refreshMacros()
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
}
