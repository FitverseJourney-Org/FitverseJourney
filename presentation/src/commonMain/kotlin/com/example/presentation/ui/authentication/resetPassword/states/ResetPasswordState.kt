package com.example.presentation.ui.authentication.resetPassword.states

import com.example.domain.models.snackbar.SnackBarData
import com.example.presentation.ui.authentication.register.states.snackbar.SnackbarEvent

data class ResetPasswordState(
    val email: String = "",
    val snackbarEvent: SnackbarEvent? = null,
    val isLoading: Boolean = false,
    val isEmailSent: Boolean = false,
    val snackBarData: SnackBarData? = null,
    val errors: List<String> = emptyList()
)