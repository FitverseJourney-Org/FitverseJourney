package org.fitverse.presentation.expect

// ─────────────────────────────────────────────────────────────────────────────
// TIMERS — Cronômetro, tempo de treino e formatação de duração.
// actual: androidMain → System.currentTimeMillis / Calendar / String.format
//         iosMain     → NSDate / NSCalendar / NSString.stringWithFormat
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Fonte de tempo da plataforma — epoch e calendário.
 *
 * ```
 * TimerManager.nowMillis()       → 1745580000000
 * TimerManager.getCurrentYear()  → 2026
 * ```
 */
expect object TimerManager {
    /** Epoch atual em milissegundos. */
    fun nowMillis(): Long
    /** Ano atual do calendário do dispositivo. */
    fun getCurrentYear(): Int
}

/**
 * Formata [seconds] no padrão `mm:ss` para timers de treino e descanso.
 *
 * ```
 * formatWorkoutTime(0)     → "00:00"
 * formatWorkoutTime(90)    → "01:30"
 * formatWorkoutTime(3661)  → "61:01"
 * ```
 */
expect fun formatWorkoutTime(seconds: Int): String

/**
 * Formata [seconds] como duração legível (h / min / s).
 *
 * ```
 * formatDuration(45)    → "45s"
 * formatDuration(90)    → "1min 30s"
 * formatDuration(3661)  → "1h 1min 1s"
 * ```
 */
expect fun formatDuration(seconds: Long): String

// ── Extensions ────────────────────────────────────────────────────────────────

/** `90.toTimerString()` → `"01:30"` */
fun Int.toTimerString(): String = formatWorkoutTime(this)

/** `3661L.toDurationString()` → `"1h 1min 1s"` */
fun Long.toDurationString(): String = formatDuration(this)
