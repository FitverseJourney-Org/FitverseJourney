package com.example.presentation.screens.ui.dashboard.util

import androidx.compose.runtime.Composable
import com.example.expect.DateTimeFormatter.getHourOfDay

@Composable
fun getGreeting(): String {
    return when (getHourOfDay()) {
        in 5..11 -> "Good morning"    // 05:00 - 11:59
        in 12..17 -> "Good afternoon" // 12:00 - 17:59
        in 18..22 -> "Good evening"   // 18:00 - 22:59
        else -> "Good night"          // 23:00 - 04:59 (Madrugada/Noite tardia)
    }
}