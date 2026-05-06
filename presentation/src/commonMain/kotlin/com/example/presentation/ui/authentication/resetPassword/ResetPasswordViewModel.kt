package com.example.presentation.ui.authentication.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.reset.ResetPasswordUseCase
import com.example.domain.models.snackbar.SnackBarData
import com.example.domain.models.snackbar.SnackbarType
import com.example.presentation.ui.authentication.resetPassword.states.ResetPasswordIntent
import com.example.presentation.ui.authentication.resetPassword.states.ResetPasswordState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ResetPasswordState())
    val state: StateFlow<ResetPasswordState> = _state

    fun onIntent(intent: ResetPasswordIntent) {
        when (intent) {
            is ResetPasswordIntent.EmailChanged -> {
                _state.update { it.copy(email = intent.value) }
            }
            ResetPasswordIntent.BtnSubmit -> submitResetPassword()
            ResetPasswordIntent.BtnBack -> { /* tratado via callback na UI */ }
        }
    }

    private fun submitResetPassword() {
        if (_state.value.email.isEmpty()) {
            _state.update {
                it.copy(
                    snackBarData = SnackBarData(
                        message = "Insira seu e-mail.",
                        type = SnackbarType.ERROR
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            resetPasswordUseCase(_state.value.email)
                .fold(
                    onSuccess = {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isEmailSent = true,
                            )
                        }
                    },
                    onFailure = { error ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                snackBarData = SnackBarData(
                                    message = error.message ?: "Erro ao enviar e-mail.",
                                    type = SnackbarType.ERROR
                                )
                            )
                        }
                    }
                )
        }
    }

    fun onSnackbarConsumed() {
        _state.update { it.copy(snackBarData = null) }
    }
}