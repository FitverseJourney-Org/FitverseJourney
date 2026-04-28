package com.example.domain.models.local.language

import org.jetbrains.compose.resources.DrawableResource

data class AppLanguageItem(
    val name: String,
    val code: TagLanguage,
    val flagRes: DrawableResource
){
    companion object {

    }
}
