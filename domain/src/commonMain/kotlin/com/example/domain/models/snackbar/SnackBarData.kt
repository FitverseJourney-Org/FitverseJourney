package org.fitverse.domain.models.snackbar

import org.fitverse.domain.expect.DateTimerManager

data class SnackBarData(
    val id: Long = DateTimerManager.currentTimeMillis(),
    val message: String,
    val type: SnackbarType = SnackbarType.INFO
)