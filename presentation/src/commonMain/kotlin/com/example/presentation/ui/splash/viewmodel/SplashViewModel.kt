package com.example.presentation.ui.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.db.datastore.authentication.ObserveIsAuthenticatedUseCase
import com.example.domain.usecase.db.datastore.onboarding.ObserveOnboardingCompletedUseCase
import com.example.domain.usecase.db.datastore.trial.ObserveIsTrialCompletedUseCase
import com.example.presentation.navigationState.SplashNavigation
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val observeIsAuthenticatedUseCase: ObserveIsAuthenticatedUseCase,
    private val observeOnboardingCompletedUseCase: ObserveOnboardingCompletedUseCase,
    private val observeTrialCompletedUseCase: ObserveIsTrialCompletedUseCase
) : ViewModel() {

    private val _events = Channel<SplashNavigation>()
    val navigationState = _events.receiveAsFlow()

    init {
        decideInitialDestination()
    }

    private fun decideInitialDestination() {
        viewModelScope.launch {
            try {
                val deferredOnboarding = async { observeOnboardingCompletedUseCase().first() }
                val deferredAuth       = async { observeIsAuthenticatedUseCase().first() }
                val deferredTrial      = async { observeTrialCompletedUseCase().first() }

                delay(1500)

                val destination = when {
                    !deferredOnboarding.await() -> SplashNavigation.ToOnboarding
                    !deferredTrial.await()      -> SplashNavigation.ToTrial
                    !deferredAuth.await()       -> SplashNavigation.ToAuth
                    else                        -> SplashNavigation.ToHome
                }
                _events.send(destination)

            } catch (e: Exception) {
                _events.send(SplashNavigation.ToAuth)
            }
        }
    }
}