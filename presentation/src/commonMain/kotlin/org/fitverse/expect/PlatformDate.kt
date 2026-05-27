package org.fitverse.presentation.expect

// ─────────────────────────────────────────────────────────────────────────────
// DATAS — Formatação de datas, tempo relativo e utilitários de calendário.
// actual: androidMain → SimpleDateFormat / java.util.Calendar
//         iosMain     → NSDateFormatter / NSCalendar
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Utilitários de data orientados ao calendário do dispositivo.
 *
 * ```
 * DateFormatter.getTodayIsoDate()                → "2026-04-25"
 * DateFormatter.getFormattedDate(0)              → "abr 25, 2026"
 * DateFormatter.getFormattedDate(1)              → "abr 26, 2026"
 * DateFormatter.formatFullDate(1745580000000L)   → "Sábado, abr 25, 2026 at 14:00"
 * DateFormatter.getCurrentTimeMillis()           → 1745580000000
 * ```
 */
expect object DateFormatter {

    /**
     * Data com [daysOffset] dias a partir de hoje, no locale do dispositivo.
     *
     * ```
     * getFormattedDate(0)   → "abr 25, 2026"   — hoje
     * getFormattedDate(1)   → "abr 26, 2026"   — amanhã
     * getFormattedDate(-1)  → "abr 24, 2026"   — ontem
     * ```
     */
    fun getFormattedDate(daysOffset: Int = 0): String

    /**
     * Data e hora completas por extenso a partir de [timestamp] em ms.
     *
     * ```
     * formatFullDate(1745580000000L)  → "Sábado, abr 25, 2026 at 14:00"
     * ```
     */
    fun formatFullDate(timestamp: Long): String

    /**
     * Hoje em ISO-8601 — usado em queries de banco de dados.
     *
     * ```
     * getTodayIsoDate()  → "2026-04-25"
     * ```
     */
    fun getTodayIsoDate(): String

    /**
     * Epoch atual em milissegundos — usado para timestamps e comparações.
     *
     * ```
     * getCurrentTimeMillis()  → 1745580000000
     * ```
     */
    fun getCurrentTimeMillis(): Long
}

/**
 * Formata [epochMillis] como data curta localizada em pt-BR.
 *
 * ```
 * formatDate(1747353600000L)  → "15 Mai 2025"
 * ```
 */
expect fun formatDate(epochMillis: Long): String

/**
 * Formata [epochMillis] como tempo relativo em pt-BR.
 *
 * ```
 * formatRelativeTime(now - 60_000)      → "há 1 minuto"
 * formatRelativeTime(now - 7_200_000)   → "há 2 horas"
 * formatRelativeTime(now - 86_400_000)  → "ontem"
 * formatRelativeTime(now - 864_000_000) → "há 10 dias"
 * ```
 */
expect fun formatRelativeTime(epochMillis: Long): String
