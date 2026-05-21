package org.fitverse.presentation.ui.authentication.resetPassword.states

sealed interface ResetPasswordNavigation {
    data object NavigateBack : ResetPasswordNavigation
}
