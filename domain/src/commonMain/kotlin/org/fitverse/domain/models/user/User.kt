package org.fitverse.domain.models.user

import org.fitverse.domain.expect.AgeCalculator
import org.fitverse.domain.expect.fromBirthMillis

data class User(
    val uid       : String  = "",
    val name      : String,
    val lastname  : String,
    val username  : String,
    val email     : String,
    val genero    : Genero,
    val birthDate : Long,           // epoch millis — alinhado com UserEntity_v2 (INTEGER)
    val weight    : Double,
    val height    : Int,
    val isPremium : Boolean = false,
    val photoUrl  : String  = "",
    val createdAt : Long,
    val updatedAt : Long,
) {
    val age: Int get() = AgeCalculator.fromBirthMillis(birthDate)
}
