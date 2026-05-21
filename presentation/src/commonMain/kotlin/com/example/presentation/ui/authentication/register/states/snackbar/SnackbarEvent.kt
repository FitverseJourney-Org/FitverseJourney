package org.fitverse.presentation.ui.authentication.register.states.snackbar

data class SnackbarEvent(
    val message: String,
    val isError: Boolean = false,
)