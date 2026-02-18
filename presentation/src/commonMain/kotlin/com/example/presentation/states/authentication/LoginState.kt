package com.example.presentation.states.authentication

import com.example.domain.model.dbLocal.language.Language
import com.example.presentation.components.snackbar.SnackBarData
import com.example.presentation.core.utils.LanguageAvailableApp.availableLanguages
import org.jetbrains.compose.resources.DrawableResource

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailErrors: List<String> = emptyList(),
    val passwordErrors: List<String> = emptyList(),
    val language: Language = availableLanguages.first(),
    val imageLanguage: DrawableResource? = null,
    val isPasswordVisible: Boolean = false,
    val snackBarData: SnackBarData? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
)