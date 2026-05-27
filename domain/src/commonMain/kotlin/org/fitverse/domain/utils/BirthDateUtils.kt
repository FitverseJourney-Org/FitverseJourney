package org.fitverse.domain.utils

// Algoritmo Julian Day Number — aritmética pura inteira, sem bibliotecas externas

/** Converte "DD/MM/YYYY" → epoch millis. Retorna 0 em caso de falha. */
fun String.toEpochMillis(): Long = try {
    val parts = split("/")
    val d = parts[0].toInt()
    val m = parts[1].toInt()
    val y = parts[2].toInt()
    val a  = (14 - m) / 12
    val yr = y + 4800 - a
    val mn = m + 12 * a - 3
    val jdn = d + (153 * mn + 2) / 5 + yr * 365 + yr / 4 - yr / 100 + yr / 400 - 32045
    (jdn - 2440588L) * 86_400_000L
} catch (_: Exception) { 0L }

/** Converte epoch millis → "DD/MM/YYYY". */
fun Long.toBirthDateString(): String {
    val jdn = this / 86_400_000L + 2440588L
    val l   = jdn + 68569
    val n   = 4 * l / 146097
    val l2  = l - (146097 * n + 3) / 4
    val i   = 4000 * (l2 + 1) / 1461001
    val l3  = l2 - 1461 * i / 4 + 31
    val j   = 80 * l3 / 2447
    val day   = (l3 - 2447 * j / 80).toInt()
    val l4    = j / 11
    val month = (j + 2 - 12 * l4).toInt()
    val year  = (100 * (n - 49) + i + l4).toInt()
    return "${day.toString().padStart(2, '0')}/" +
           "${month.toString().padStart(2, '0')}/" +
           year.toString().padStart(4, '0')
}
