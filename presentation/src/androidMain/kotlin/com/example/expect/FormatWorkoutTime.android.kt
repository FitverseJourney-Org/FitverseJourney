package com.example.expect

import android.annotation.SuppressLint
import java.lang.String.format

@SuppressLint("DefaultLocale")
actual fun formatWorkoutTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return format("%02d:%02d", min, sec)
}