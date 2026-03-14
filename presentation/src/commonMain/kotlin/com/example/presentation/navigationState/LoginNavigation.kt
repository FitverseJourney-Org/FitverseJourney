package com.example.presentation.navigationState

sealed class LoginNavigation {
    object ToRegister : LoginNavigation()
    object ToResetPassword : LoginNavigation()
    object ToHome : LoginNavigation()
}