package org.fitverse.domain.expect

import org.fitverse.domain.utils.toBirthDateString

expect object AgeCalculator {
    /** Calcula idade a partir de "DD/MM/YYYY". Usado na validação do registro. */
    fun fromBirthDate(birthDate: String): Int
}

/** Calcula idade a partir de epoch millis — delega para fromBirthDate sem expect/actual extra. */
fun AgeCalculator.fromBirthMillis(millis: Long): Int = fromBirthDate(millis.toBirthDateString())
