package com.example.domain.models.snackbar

data class SnackBarData(
    val message: String,
    val type: SnackbarType = SnackbarType.INFO
)