package com.example.presentation.screens.ui.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.database.datastore.authentication.ObserveIsAuthenticatedUseCase
import com.example.domain.usecase.database.datastore.onboarding.ObserveOnboardingCompletedUseCase
import com.example.presentation.navigationState.SplashNavigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(
    // Removido o repositório, usamos apenas os Use Cases para manter a Clean Arch
    private val observeIsAuthenticatedUseCase: ObserveIsAuthenticatedUseCase,
    private val observeOnboardingCompletedUseCase: ObserveOnboardingCompletedUseCase
) : ViewModel() {

    private val _navigationState = MutableSharedFlow<SplashNavigation>()
    val navigationState = _navigationState.asSharedFlow()

    init {
        decideInitialDestination()
    }

    private fun decideInitialDestination() {
        viewModelScope.launch {
            // 1. Garantimos um tempo mínimo de Splash para branding
            delay(1500)

            try {
                // 2. Coletamos apenas o primeiro valor emitido pelos fluxos do DataStore
                // Assumindo que os Use Cases retornam Flow<Boolean>
                val isOnboardingCompleted = observeOnboardingCompletedUseCase().first()
                val isAuthenticated = observeIsAuthenticatedUseCase().first()

                // 3. Lógica de roteamento priorizando o Onboarding
                val destination = when {
                    !isOnboardingCompleted -> SplashNavigation.ToOnboarding
                    !isAuthenticated -> SplashNavigation.ToAuth
                    else -> SplashNavigation.ToHome
                }

                _navigationState.emit(destination)

            } catch (e: Exception) {
                // Caso ocorra erro na leitura do DataStore, você pode decidir um fallback
                _navigationState.emit(SplashNavigation.ToAuth)
            }
        }
    }
}