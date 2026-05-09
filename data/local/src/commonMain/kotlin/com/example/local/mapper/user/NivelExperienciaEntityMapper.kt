package com.example.local.mapper.user

import com.example.domain.models.user.NivelExperiencia

// -------------------------------------------------------------
// NivelExperiencia
// -------------------------------------------------------------
fun String.toNivelExperienciaDomain(): NivelExperiencia =
    NivelExperiencia.entries.firstOrNull { it.name == this } ?: NivelExperiencia.INICIANTE
fun NivelExperiencia.toEntityString(): String = this.name
