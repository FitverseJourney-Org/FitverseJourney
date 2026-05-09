package com.example.expect

import java.util.Calendar

actual object TimerManager {
    actual fun nowMillis(): Long = System.currentTimeMillis()
    actual fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
}