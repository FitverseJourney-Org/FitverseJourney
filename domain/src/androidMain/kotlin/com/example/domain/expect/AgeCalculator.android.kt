package com.example.domain.expect

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

actual object AgeCalculator {
    actual fun fromBirthDate(birthDate: String): Int {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val birth = LocalDate.parse(birthDate, formatter)
        return Period.between(birth, LocalDate.now()).years
    }
}