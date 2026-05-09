package com.example.local.mapper.user

import com.example.domain.models.user.ClassType

fun String.toClassTypeDomain(): ClassType =
    ClassType.entries.firstOrNull { it.name == this }
        ?: ClassType.TITAN // fallback seguro

fun ClassType.toEntityString(): String = this.name