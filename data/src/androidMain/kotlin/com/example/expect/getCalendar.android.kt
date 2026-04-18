package com.example.expect

import java.time.Instant
import java.time.ZoneId

// ─────────────────────────────────────────────────────────────────────────────
// androidMain — implementação com java.time
// ─────────────────────────────────────────────────────────────────────────────
// Arquivo: androidMain/kotlin/com/example/expect/PlatformDate.android.kt
actual class PlatformDate(private val instant: Instant) {
    private val local = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    actual val day: Int          get() = local.dayOfMonth
    actual val month: Int        get() = local.monthValue          // 1-based
    actual val year: Int         get() = local.year
    actual val epochMillis: Long get() = instant.toEpochMilli()
}

actual fun Long.toPlatformDate(): PlatformDate =
    PlatformDate(Instant.ofEpochMilli(this))
