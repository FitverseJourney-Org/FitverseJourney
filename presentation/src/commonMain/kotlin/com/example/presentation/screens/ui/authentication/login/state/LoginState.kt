package com.example.presentation.screens.ui.authentication.login.state

import com.example.presentation.components.snackbar.SnackBarData

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailErrors: List<String> = emptyList(),
    val passwordErrors: List<String> = emptyList(),
    val isPasswordVisible: Boolean = false,
    val snackBarData: SnackBarData? = null,
    val isLoading: Boolean = false,
    val processLogin: Boolean = false,
    val isLoggedIn: Boolean = false
)