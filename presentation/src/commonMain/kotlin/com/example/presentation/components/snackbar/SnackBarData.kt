package com.example.presentation.components.snackbar

data class SnackBarData(
    val message: String,
    val type: SnackbarType = SnackbarType.INFO
)
