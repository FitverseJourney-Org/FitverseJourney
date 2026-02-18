package com.example.presentation.core.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

object TimeProvider {

    fun nowLocalTime(): String {
        val now = Clock.System.now()
        val hour = now.toLocalDateTime(TimeZone.currentSystemDefault()).hour.toString().padStart(2, '0')
        val min = now.toLocalDateTime(TimeZone.currentSystemDefault()).minute.toString().padStart(2, '0')

        return "$hour:$min"
    }
}

