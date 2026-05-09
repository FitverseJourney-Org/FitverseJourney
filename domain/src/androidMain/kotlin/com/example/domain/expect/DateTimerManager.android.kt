package com.example.domain.expect

actual object DateTimerManager {
    actual fun currentTimeMillis(): Long = System.currentTimeMillis()
}