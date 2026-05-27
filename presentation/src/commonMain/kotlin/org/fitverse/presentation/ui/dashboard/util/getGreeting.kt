package org.fitverse.presentation.ui.dashboard.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.fitverse.presentation.expect.TimerManager
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun getGreeting(): String {
    val hour = remember {
        Instant.fromEpochMilliseconds(TimerManager.nowMillis())
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .hour
    }
    return when (hour) {
        in 5..11  -> "Good morning"
        in 12..17 -> "Good afternoon"
        in 18..22 -> "Good evening"
        else      -> "Good night"
    }
}
