package org.fitverse.domain.expect

expect object AgeCalculator {
    fun fromBirthDate(birthDate: String): Int // formato esperado: "dd/MM/yyyy"
}