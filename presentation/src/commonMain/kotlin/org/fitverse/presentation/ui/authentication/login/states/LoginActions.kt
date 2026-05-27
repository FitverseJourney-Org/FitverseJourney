package org.fitverse.presentation.ui.authentication.login.states

sealed interface LoginAction {
    data class EmailChanged(val value: String) : LoginAction
    data class PasswordChanged(val value: String) : LoginAction
    data object TogglePasswordVisibility : LoginAction
    data object LoginClicked : LoginAction
    data object NavigateToRegister : LoginAction
    data object NavigateToForgotPassword : LoginAction
    data object NavigateToHome : LoginAction
}