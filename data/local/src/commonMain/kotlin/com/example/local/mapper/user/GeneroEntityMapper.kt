package com.example.local.mapper.user

import com.example.domain.models.user.Genero

// -------------------------------------------------------------
// Gender
// -------------------------------------------------------------
fun String.toGeneroDomain(): Genero =
    Genero.entries.firstOrNull { it.name == this } ?: Genero.MASCULINO
fun Genero.toEntityString(): String = this.name
