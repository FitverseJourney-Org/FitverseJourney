package com.example.presentation.navigations

sealed class LoginNavigation {
    object ToRegister : LoginNavigation()
    object ToResetPassword : LoginNavigation()
    object ToHome : LoginNavigation()
}