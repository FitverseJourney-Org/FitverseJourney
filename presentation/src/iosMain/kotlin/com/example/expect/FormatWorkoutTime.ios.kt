package com.example.expect


import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun formatWorkoutTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60

    return NSString.stringWithFormat("%02d:%02d", min, sec)
}