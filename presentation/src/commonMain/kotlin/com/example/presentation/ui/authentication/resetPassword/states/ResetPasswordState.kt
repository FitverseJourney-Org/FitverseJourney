package org.fitverse.presentation.ui.authentication.resetPassword.states

import org.fitverse.domain.models.snackbar.SnackBarData

data class ResetPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isEmailSent: Boolean = false,
    val snackBarData: SnackBarData? = null,
    val errors: List<String> = emptyList()
)
