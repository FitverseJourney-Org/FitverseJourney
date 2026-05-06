package com.example.presentation.ui.authentication.login.states

sealed class LoginAction {
    data class EmailChanged(val value: String) : LoginAction()
    data class PasswordChanged(val value: String) : LoginAction()
    object TogglePasswordVisibility : LoginAction()
    object LoginClicked : LoginAction()

    object NavigateToRegister: LoginAction()
    object NavigateToForgotPassword: LoginAction()
    object NavigateToHome : LoginAction()

}