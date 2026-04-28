package com.example.domain.models.auth.register.snackbar

data class SnackbarEvent(
    val message: String,
    val isError: Boolean = false,
)