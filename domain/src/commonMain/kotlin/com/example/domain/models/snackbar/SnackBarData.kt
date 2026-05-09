package com.example.domain.models.snackbar

import com.example.domain.expect.DateTimerManager
import kotlin.time.Clock

data class SnackBarData(
    val id: Long = DateTimerManager.currentTimeMillis(),
    val message: String,
    val type: SnackbarType = SnackbarType.INFO
)