package org.fitverse.data.local.mapper.user

import org.fitverse.domain.models.user.Genero

// DB usa MALE/FEMALE/OTHER (CHECK constraint do UserEntity_v2)
// Domain usa MASCULINO/FEMININO/OUTRO

fun String.toGeneroDomain(): Genero = when (this) {
    "MALE"   -> Genero.MASCULINO
    "FEMALE" -> Genero.FEMININO
    "OTHER"  -> Genero.OUTRO
    else     -> Genero.MASCULINO
}

fun Genero.toEntityString(): String = when (this) {
    Genero.MASCULINO -> "MALE"
    Genero.FEMININO  -> "FEMALE"
    Genero.OUTRO     -> "OTHER"
}
