package com.example.presentation.screens.ui.authentication.login.state

import com.example.domain.model.local.language.AppLanguageItem
import com.example.presentation.components.snackbar.SnackBarData
import com.example.presentation.core.utils.LanguageAvailableApp

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