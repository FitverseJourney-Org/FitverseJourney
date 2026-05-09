package com.example.local.mapper.user

import com.example.domain.models.user.Objetivo

// -------------------------------------------------------------
// Objetivo
// -------------------------------------------------------------
fun String.toObjetivoDomain(): Objetivo =
    Objetivo.entries.firstOrNull { it.name == this } ?: Objetivo.VIDA_SAUDAVEL
fun Objetivo.toEntityString(): String = this.name