package org.fitverse.data.local.mapper.user

import org.fitverse.domain.models.user.ClassType

fun String.toClassTypeDomain(): ClassType = ClassType.entries.firstOrNull { it.name == this } ?: ClassType.TITAN // fallback seguro
fun ClassType.toEntityString(): String = this.name