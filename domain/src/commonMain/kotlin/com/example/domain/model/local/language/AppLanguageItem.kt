package com.example.domain.model.local.language

import org.jetbrains.compose.resources.DrawableResource

data class AppLanguageItem(
    val name: String,
    val code: TagLanguage,
    val flagRes: DrawableResource
){
    companion object {

    }
}
