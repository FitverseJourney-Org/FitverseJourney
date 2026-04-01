package com.example.presentation.screens.ui.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.dbLocal.datastore.AppPreferencesRepository
import com.example.presentation.screens.ui.authentication.login.actions.LoginAction
import com.example.presentation.navigationState.SplashNavigation
import com.example.presentation.screens.ui.splash.actions.SplashActions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(
    private val preferencesRepository: AppPreferencesRepository
) : ViewModel() {

    private val _navigationState = MutableSharedFlow<SplashNavigation>()
    val navigationState = _navigationState.asSharedFlow()

    // Nota rápida: Notei que a tipagem aqui é LoginAction, talvez seja um resquício de outro arquivo.
    // Se não for usar na Splash, pode remover com segurança!
    private val _uiAction = MutableSharedFlow<LoginAction>(replay = 0)
    val uiAction: SharedFlow<LoginAction> = _uiAction

    init {
        // Assim que o ViewModel nasce, ele decide a rota automaticamente
        decideInitialDestination()
    }

    private fun decideInitialDestination() {
        viewModelScope.launch {
            // Opcional: Um pequeno delay para a splash screen não piscar muito rápido
            // caso a leitura do DataStore seja instantânea
            delay(1500)

            // Usamos .first() para ler o valor apenas uma vez na inicialização,
            // em vez de .collect() que ficaria escutando mudanças para sempre.
            val hasCompletedOnboarding = preferencesRepository.isOnboardingCompleted.first()
            val isAuthenticated = preferencesRepository.isAuthenticated.first()

            // Lógica de roteamento
            when {
                !hasCompletedOnboarding -> {
                    _navigationState.emit(SplashNavigation.ToOnboarding)
                }
                !isAuthenticated -> {
                    // Se o usuário não está autenticado, mandamos para o fluxo de Auth.
                    // (Aqui você também pode adaptar a regra para o Trial, se necessário)
                    _navigationState.emit(SplashNavigation.ToAuth)
                }
                else -> {
                    _navigationState.emit(SplashNavigation.ToHome)
                }
            }
        }
    }
}