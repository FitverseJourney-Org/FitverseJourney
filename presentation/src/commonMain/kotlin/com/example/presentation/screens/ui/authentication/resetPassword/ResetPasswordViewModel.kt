package com.example.presentation.screens.ui.authentication.resetPassword

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.authentication.reset.ResetPasswordUseCase
import com.example.presentation.components.snackbar.SnackBarData
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.screens.ui.authentication.resetPassword.state.ResetPasswordState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel(){
    var _state = MutableStateFlow(ResetPasswordState())
    val state: StateFlow<ResetPasswordState> = _state
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun onEmailChange(newValue: String) {
        _state.update { it.copy(email = newValue) }

    }


    fun onClickResetPassword() {
        if(_state.value.email.isNotEmpty()){
            processRepository()
        }else{
            _state.update {
                it.copy(
                    snackBarData = SnackBarData(
                        message = "Please enter your email.",
                        type = SnackbarType.ERROR
                    )
                )
            }
        }
    }

    fun processRepository(){
        scope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            val result = resetPasswordUseCase(_state.value.email)
            _state.update { state ->
                result.fold(
                    onSuccess = {
                        state.copy(
                            isLoading = false,
                            snackBarData = SnackBarData(
                                message = "Check your email to reset your password.",
                                type = SnackbarType.SUCCESS
                            )
                        )
                    },
                    onFailure = { error ->
                        state.copy(
                            isLoading = false,
                            snackBarData = SnackBarData(
                                message = "Check your email to reset your password.",
                                type = SnackbarType.SUCCESS
                            )
                        )
                    }
                )
            }
        }
    }

    fun cleanSnackBar() {
        _state.update {
            it.copy(snackBarData = null)
        }

    }
}