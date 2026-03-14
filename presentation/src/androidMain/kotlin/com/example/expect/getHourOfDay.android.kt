package com.example.expect

import java.util.Calendar

actual fun getHourOfDay(): Int {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
}