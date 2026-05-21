package org.fitverse.presentation.ui.authentication.resetPassword.states

sealed interface ResetPasswordAction {
    data class EmailChanged(val value: String) : ResetPasswordAction
    data object BtnSubmit : ResetPasswordAction
    data object BtnBack : ResetPasswordAction
}
