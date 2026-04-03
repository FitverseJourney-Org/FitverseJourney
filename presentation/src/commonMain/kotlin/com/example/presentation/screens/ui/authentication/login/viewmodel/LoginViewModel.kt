package com.example.presentation.screens.ui.authentication.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presentation.screens.ui.authentication.login.actions.LoginAction
import com.example.domain.usecase.login.LoginUseCase
import com.example.presentation.components.snackbar.SnackBarData
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.navigationState.LoginNavigation
import com.example.presentation.screens.ui.authentication.login.state.LoginState
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
            else -> {

            } 
        }
    }

    private fun navigateTo(destination: LoginNavigation) {
        viewModelScope.launch { _navigationState.emit(destination) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            loginUseCase(
                _state.value.email,
                _state.value.password
            ).fold(
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