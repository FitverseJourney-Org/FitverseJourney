package com.example.domain.model.dbLocal.language

enum class TagLanguage(val iso: String) {

    SYSTEM("system"),

    ENGLISH("en"),
    GERMAN("de"),
    SPANISH("es"),
    FRENCH("fr"),
    KOREAN("ko"),
    PORTUGUESE("pt"),
    RUSSIAN("ru");

    companion object {

        fun fromIso(iso: String): TagLanguage {
            // se veio "system", respeita
            if (iso == SYSTEM.iso) return SYSTEM

            // tenta match direto
            values().firstOrNull { it.iso == iso }?.let { return it }

            // tenta match por prefixo (pt-BR -> pt)
            val normalized = iso.substringBefore("-")
            return values().firstOrNull { it.iso == normalized }
                ?: SYSTEM
        }
    }
}