package com.example.presentation.presenter.onboarding

import com.example.presentation.states.onboarding.OnboardingState
import fitversejorneyapp.presentation.generated.resources.Res
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingPresenter() {
    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)



    fun nextPage() {
        _state.update {
            it.copy(
                currentPage = it.currentPage + 1
            )
        }
    }

    fun skipToLastPage() {
        _state.update {
            it.copy(
                currentPage = it.totalPages - 1
            )
        }
    }


    private fun processOnboardingData() {
        scope.launch {

        }
    }
}