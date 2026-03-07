package com.example.domain.model.authentication.login

import com.example.domain.model.dbLocal.language.Language

sealed class LoginAction {
    data class EmailChanged(val value: String) : LoginAction()
    data class PasswordChanged(val value: String) : LoginAction()
    data class LanguageChanged(val value: Language) : LoginAction()

    object TogglePasswordVisibility : LoginAction()
    object LoginClicked : LoginAction()

    object NavigateToRegister: LoginAction()
    object NavigateToForgotPassword: LoginAction()
    object NavigateToHome : LoginAction()

}