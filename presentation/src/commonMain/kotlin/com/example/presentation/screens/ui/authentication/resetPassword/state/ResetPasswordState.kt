package com.example.presentation.screens.ui.authentication.resetPassword.state

import com.example.presentation.components.snackbar.SnackBarData

data class ResetPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isEmailSent: Boolean = false,
    val snackBarData: SnackBarData? = null,
    val errors: List<String> = emptyList()
)