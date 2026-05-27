package org.fitverse.presentation.ui.authentication.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.domain.models.snackbar.SnackBarData
import org.fitverse.domain.models.snackbar.SnackbarType
import org.fitverse.domain.usecase.reset.ResetPasswordUseCase
import org.fitverse.presentation.ui.authentication.resetPassword.states.ResetPasswordAction
import org.fitverse.presentation.ui.authentication.resetPassword.states.ResetPasswordNavigation
import org.fitverse.presentation.ui.authentication.resetPassword.states.ResetPasswordState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ResetPasswordState())
    val state: StateFlow<ResetPasswordState> = _state.asStateFlow()

    private val _navigation = Channel<ResetPasswordNavigation>()
    val navigationState = _navigation.receiveAsFlow()

    fun onAction(action: ResetPasswordAction) {
        when (action) {
            is ResetPasswordAction.EmailChanged -> _state.update { it.copy(email = action.value) }
            ResetPasswordAction.BtnSubmit -> submitResetPassword()
            ResetPasswordAction.BtnBack -> viewModelScope.launch {
                _navigation.send(ResetPasswordNavigation.NavigateBack)
            }
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
                        _state.update { it.copy(isLoading = false, isEmailSent = true) }
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
