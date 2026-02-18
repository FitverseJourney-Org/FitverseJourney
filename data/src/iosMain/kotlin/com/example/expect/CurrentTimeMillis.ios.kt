package com.example.expect

import platform.Foundation.NSDate
import platform.Foundation.date
import platform.Foundation.timeIntervalSince1970

actual fun currentTimeMillis(): Long {
    return (NSDate.date().timeIntervalSince1970 * 1000.0).toLong()
}