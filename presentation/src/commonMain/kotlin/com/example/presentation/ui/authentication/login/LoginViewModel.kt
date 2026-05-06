package com.example.presentation.ui.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.expect.DateTimerManager
import com.example.domain.models.snackbar.SnackBarData
import com.example.domain.models.snackbar.SnackbarType
import com.example.domain.usecase.login.LoginUseCase
import com.example.presentation.navigationState.LoginNavigation
import com.example.presentation.ui.authentication.login.states.LoginAction
import com.example.presentation.ui.authentication.login.states.LoginState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _navigationState = MutableSharedFlow<LoginNavigation>()
    val navigationState = _navigationState.asSharedFlow()

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.EmailChanged -> _state.update { it.copy(email = action.value) }
            is LoginAction.PasswordChanged -> _state.update { it.copy(password = action.value) }
            is LoginAction.TogglePasswordVisibility -> onTogglePasswordVisibility()
            is LoginAction.LoginClicked -> onLoginClick()
            is LoginAction.NavigateToRegister -> navigateTo(LoginNavigation.ToRegister)
            is LoginAction.NavigateToForgotPassword -> navigateTo(LoginNavigation.ToResetPassword)
            is LoginAction.NavigateToHome -> navigateTo(LoginNavigation.ToHome)
        }
    }

    private fun navigateTo(destination: LoginNavigation) {
        viewModelScope.launch { _navigationState.emit(destination) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        val email = _state.value.email
        val password = _state.value.password

        // Validação de UI preventiva
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(
                snackBarData = SnackBarData(
                    DateTimerManager.currentTimeMillis(),
                    "Por favor, preencha e-mail e senha",
                    SnackbarType.ERROR
                )
            )}
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            loginUseCase(email, password).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false) }
                    _navigationState.emit(LoginNavigation.ToHome)
                },
                onFailure = { error ->
                    _state.update { s ->
                        s.copy(
                            isLoading = false,
                            snackBarData = SnackBarData(
                                message = error.message ?: "Erro desconhecido",
                                type = SnackbarType.ERROR
                            )
                        )
                    }
                }
            )
        }
    }

    fun consumeSnackBar() {
        _state.update { it.copy(snackBarData = null) }
    }
}