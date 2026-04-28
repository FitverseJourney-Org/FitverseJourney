package com.example.presentation.utils

import androidx.compose.runtime.Composable
import com.example.expect.LocalAppLocale

@Composable
fun getLocaleDate(): String {
    return LocalAppLocale.current
}

@Composable
fun getLocaleDatePattern(): String {
    val locale = getLocaleDate()
    return when (locale) {
        "US", "CA", "PH" -> "MM/DD/YYYY" // mês primeiro
        "JP", "KR", "CN", "HU", "LT", "BT" -> "YYYY/MM/DD" // ano primeiro
        else -> "DD/MM/YYYY" // maioria dos países (BR, PT, EU, etc.)
    }
}