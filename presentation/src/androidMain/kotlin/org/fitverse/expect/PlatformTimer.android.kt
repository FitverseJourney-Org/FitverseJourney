package org.fitverse.presentation.expect

import android.annotation.SuppressLint
import java.lang.String.format
import java.util.Calendar

// ── TimerManager ──────────────────────────────────────────────────────────────

actual object TimerManager {
    actual fun nowMillis(): Long = System.currentTimeMillis()
    actual fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
}

// ── formatWorkoutTime ─────────────────────────────────────────────────────────

@SuppressLint("DefaultLocale")
actual fun formatWorkoutTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return format("%02d:%02d", min, sec)
}

// ── formatDuration ────────────────────────────────────────────────────────────

actual fun formatDuration(seconds: Long): String {
    val h   = seconds / 3600
    val min = (seconds % 3600) / 60
    val sec = seconds % 60
    return buildString {
        if (h > 0)   append("${h}h ")
        if (min > 0) append("${min}min ")
        if (sec > 0 || (h == 0L && min == 0L)) append("${sec}s")
    }.trim()
}
