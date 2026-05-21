package org.fitverse.domain.expect

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual object DateTimerManager {
    actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
}