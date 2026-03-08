package com.example.presentation.screens.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presentation.navigations.OnboardingNavigation
import com.example.presentation.states.onboarding.OnboardingState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {

    private val _navigationState = MutableSharedFlow<OnboardingNavigation>()
    val navigationState = _navigationState.asSharedFlow()

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun setCurrentPage(page: Int, lastIndex: Int) {
        val valid = page.coerceIn(0, lastIndex)
        _state.update { it.copy(currentPage = valid) }
    }

    fun nextPage(lastIndex: Int) {
        _state.update {
            it.copy(currentPage = (it.currentPage + 1).coerceAtMost(lastIndex))
        }
        println("nextPage: " + _state.value.currentPage)
    }

    fun skipToLastPage(lastIndex: Int) {
        _state.update { it.copy(currentPage = lastIndex) }

    }
    fun emitToTrial() {
        viewModelScope.launch {
            _navigationState.emit(OnboardingNavigation.ToTrial)
        }
    }
}