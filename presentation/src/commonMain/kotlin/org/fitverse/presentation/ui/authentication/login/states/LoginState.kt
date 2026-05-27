package org.fitverse.presentation.ui.authentication.login.states

import org.fitverse.domain.models.snackbar.SnackBarData

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailErrors: List<String> = emptyList(),
    val passwordErrors: List<String> = emptyList(),
    val isPasswordVisible: Boolean = false,
    val snackBarData: SnackBarData? = null,
    val isLoading: Boolean = false,
    val processLogin: Boolean = false,
    val isLoggedIn: Boolean = false,
)