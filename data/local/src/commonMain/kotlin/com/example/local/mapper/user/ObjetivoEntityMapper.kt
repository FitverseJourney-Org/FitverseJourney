package org.fitverse.data.local.mapper.user

import org.fitverse.domain.models.user.Objetivo

// -------------------------------------------------------------
// Objetivo
// -------------------------------------------------------------
fun String.toObjetivoDomain(): Objetivo =
    Objetivo.entries.firstOrNull { it.name == this } ?: Objetivo.VIDA_SAUDAVEL
fun Objetivo.toEntityString(): String = this.name