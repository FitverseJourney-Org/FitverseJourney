package com.example.domain.model.dbLocal.language

import org.jetbrains.compose.resources.DrawableResource

data class Language(
    val name: String,
    val code: TagLanguage,
    val flagRes: DrawableResource
)
