package org.fitverse.presentation.expect

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val PTBR = Locale("pt", "BR")

// ── DateFormatter ─────────────────────────────────────────────────────────────

actual object DateFormatter {

    actual fun getFormattedDate(daysOffset: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, daysOffset)
        return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(cal.time)
    }

    actual fun formatFullDate(timestamp: Long): String =
        SimpleDateFormat("EEEE, MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
            .format(Date(timestamp))

    actual fun getTodayIsoDate(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Calendar.getInstance().time)

    actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()
}

// ── formatDate ────────────────────────────────────────────────────────────────

actual fun formatDate(epochMillis: Long): String {
    val cal   = Calendar.getInstance(PTBR).apply { timeInMillis = epochMillis }
    val day   = cal.get(Calendar.DAY_OF_MONTH)
    val month = MONTHS_PT[cal.get(Calendar.MONTH)]
    val year  = cal.get(Calendar.YEAR)
    return "$day $month $year"
}

// ── formatRelativeTime ────────────────────────────────────────────────────────

actual fun formatRelativeTime(epochMillis: Long): String {
    val diff  = System.currentTimeMillis() - epochMillis
    val secs  = diff / 1_000L
    val mins  = secs / 60
    val hrs   = mins / 60
    val days  = hrs  / 24
    return when {
        secs < 60    -> "agora mesmo"
        mins < 60    -> "há ${pluralPT(mins, "minuto",  "minutos")}"
        hrs  < 24    -> "há ${pluralPT(hrs,  "hora",    "horas")}"
        days == 1L   -> "ontem"
        days < 30    -> "há ${pluralPT(days, "dia",     "dias")}"
        days < 365   -> "há ${pluralPT(days / 30,  "mês",  "meses")}"
        else         -> "há ${pluralPT(days / 365, "ano",  "anos")}"
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun pluralPT(n: Long, singular: String, plural: String) =
    if (n == 1L) "1 $singular" else "$n $plural"

private val MONTHS_PT = listOf(
    "Jan","Fev","Mar","Abr","Mai","Jun",
    "Jul","Ago","Set","Out","Nov","Dez",
)
