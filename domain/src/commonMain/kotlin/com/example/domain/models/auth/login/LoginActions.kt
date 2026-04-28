package com.example.domain.models.auth.login

sealed class LoginAction {
    data class EmailChanged(val value: String) : LoginAction()
    data class PasswordChanged(val value: String) : LoginAction()
    data class LanguageChanged(val value: String) : LoginAction()

    object TogglePasswordVisibility : LoginAction()
    object LoginClicked : LoginAction()

    object NavigateToRegister: LoginAction()
    object NavigateToForgotPassword: LoginAction()
    object NavigateToHome : LoginAction()

}