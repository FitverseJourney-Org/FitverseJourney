package com.example.domain.models.auth.register

enum class RegisterStep(
    val index: Int,
    val label: String
) {
    CONTA(0, "CONTA"),
    PERFIL(1, "PERFIL"),
    CLASSE(2, "CLASSE"),
    OBJETIVOS(3, "OBJETIVOS");

    companion object {
        fun fromIndex(index: Int) = entries.first { it.index == index }
    }
}
