package org.fitverse.data.local.mapper.user

import org.fitverse.domain.models.user.NivelExperiencia

// -------------------------------------------------------------
// NivelExperiencia
// -------------------------------------------------------------
fun String.toNivelExperienciaDomain(): NivelExperiencia = NivelExperiencia.entries.firstOrNull { it.name == this } ?: NivelExperiencia.INICIANTE
fun NivelExperiencia.toEntityString(): String = this.name
