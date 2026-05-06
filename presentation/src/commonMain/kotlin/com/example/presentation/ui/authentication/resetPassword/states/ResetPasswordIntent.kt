package com.example.presentation.ui.authentication.resetPassword.states

sealed interface ResetPasswordIntent {
    data class EmailChanged(val value: String) : ResetPasswordIntent
    data object BtnSubmit : ResetPasswordIntent
    data object BtnBack : ResetPasswordIntent
}