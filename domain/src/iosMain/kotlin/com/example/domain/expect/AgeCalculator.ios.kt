package org.fitverse.domain.expect

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents

actual object AgeCalculator {
    actual fun fromBirthDate(birthDate: String): Int {
        // Quebra a string "dd/MM/yyyy" em partes
        val parts = birthDate.split("/")
        val day = parts[0].toInt()
        val month = parts[1].toInt()
        val year = parts[2].toInt()

        // Usa NSCalendar e NSDateComponents nativos do iOS
        val calendar = NSCalendar.currentCalendar

        val birthComponents = NSDateComponents().apply {
            setDay(day.toLong())
            setMonth(month.toLong())
            setYear(year.toLong())
        }

        val birthNSDate = calendar.dateFromComponents(birthComponents) ?: return 0

        val components = calendar.components(
            NSCalendarUnitYear,
            fromDate = birthNSDate,
            toDate = NSDate(),
            options = 0u
        )

        return components.year.toInt() // "25/04/1998" → 28
    }
}