package com.example.domain.models.auth.resetPassword

import com.example.domain.models.snackbar.SnackBarData

data class ResetPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isEmailSent: Boolean = false,
    val snackBarData: SnackBarData? = null,
    val errors: List<String> = emptyList()
)