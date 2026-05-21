package org.fitverse.presentation.ui.authentication.login.states

sealed interface LoginNavigation {
    data object ToRegister : LoginNavigation
    data object ToResetPassword : LoginNavigation
    data object ToHome : LoginNavigation
}
