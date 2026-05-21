package org.fitverse.data.local.mapper.user

import org.fitverse.domain.models.user.Genero

// -------------------------------------------------------------
// Gender
// -------------------------------------------------------------
fun String.toGeneroDomain(): Genero = Genero.entries.firstOrNull { it.name == this } ?: Genero.MASCULINO
fun Genero.toEntityString(): String = this.name
