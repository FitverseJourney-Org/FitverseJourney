package com.example.presentation.screens.ui.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.authentication.login.LoginAction
import com.example.domain.usecase.authentication.login.LoginUseCase
import com.example.presentation.components.snackbar.SnackBarData
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.navigations.LoginNavigation
import com.example.presentation.screens.ui.authentication.login.state.LoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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

    private val _uiAction = MutableSharedFlow<LoginAction>(replay = 0)
    val uiAction: SharedFlow<LoginAction> = _uiAction

    fun onAction(action: LoginAction){
        when(action){
            is LoginAction.EmailChanged -> _state.update { it.copy(email = action.value) }
            is LoginAction.PasswordChanged -> _state.update { it.copy(password = action.value) }
            is LoginAction.LanguageChanged -> _state.update { it.copy(language = action.value) }
            is LoginAction.TogglePasswordVisibility -> onTogglePasswordVisibility()
            is LoginAction.LoginClicked -> onLoginClick()
            is LoginAction.NavigateToRegister -> viewModelScope.launch { _navigationState.emit(LoginNavigation.ToRegister) }
            is LoginAction.NavigateToForgotPassword -> viewModelScope.launch { _navigationState.emit(LoginNavigation.ToResetPassword) }
            is LoginAction.NavigateToHome -> viewModelScope.launch { _navigationState.emit(LoginNavigation.ToHome) }
        }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = loginUseCase(
                _state.value.email,
                _state.value.password
            )

            result.fold(
                onSuccess = {
                    // 1) atualizar UI
                    _state.update { s ->
                        s.copy(
                            isLoading = false,
                        )
                    }
                    // 2) emitir evento de navegação para a Home
                    _navigationState.emit(LoginNavigation.ToHome)
                },
                onFailure = { error ->
                    _state.update { s ->
                        s.copy(
                            isLoading = false,
                            snackBarData = SnackBarData(
                                message = error.message ?: "Something went wrong. Please try again.",
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