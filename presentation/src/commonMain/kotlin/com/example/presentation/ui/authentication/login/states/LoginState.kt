package com.example.presentation.ui.authentication.login.states

import com.example.domain.models.snackbar.SnackBarData

data class LoginState(
    var email: String = "",
    var password: String = "",
    val emailErrors: List<String> = emptyList(),
    val passwordErrors: List<String> = emptyList(),
    var isPasswordVisible: Boolean = false,
    val snackBarData: SnackBarData? = null,
    val isLoading: Boolean = false,
    val processLogin: Boolean = false,
    val isLoggedIn: Boolean = false
)