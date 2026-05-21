package org.fitverse.presentation.ui.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.domain.expect.DateTimerManager
import org.fitverse.domain.models.errors.AuthError
import org.fitverse.domain.models.snackbar.SnackBarData
import org.fitverse.domain.models.snackbar.SnackbarType
import org.fitverse.domain.usecase.login.LoginUseCase
import org.fitverse.presentation.ui.authentication.login.states.LoginNavigation
import org.fitverse.presentation.ui.authentication.login.states.LoginAction
import org.fitverse.presentation.ui.authentication.login.states.LoginState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _events = Channel<LoginNavigation>()
    val navigationState = _events.receiveAsFlow()

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
        viewModelScope.launch { _events.send(destination) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        val email    = _state.value.email.trim()
        val password = _state.value.password.trim()

        if (email.isBlank() || password.isBlank()) {
            _state.update {
                it.copy(
                    snackBarData = SnackBarData(
                        id = DateTimerManager.currentTimeMillis(),
                        message   = "Por favor, preencha e-mail e senha",
                        type      = SnackbarType.ERROR
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            loginUseCase(email, password).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(LoginNavigation.ToHome)
                },
                onFailure = { error ->
                    val msg = when (error) {
                        is AuthError.InvalidCredentials -> "E-mail ou senha incorretos"
                        is AuthError.UserNotFound       -> "Conta não encontrada"
                        is AuthError.NetworkError       -> "Verifique sua conexão"
                        is AuthError.TooManyRequests    -> "Muitas tentativas, aguarde"
                        is AuthError.WeakPassword       -> "Senha muito fraca"
                        is AuthError.ServiceUnavailable -> "Serviço indisponível"
                        else                            -> "Erro inesperado"
                    }
                    _state.update {
                        it.copy(
                            isLoading    = false,
                            snackBarData = SnackBarData(
                                id = DateTimerManager.currentTimeMillis(),
                                message   = msg,
                                type      = SnackbarType.ERROR
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