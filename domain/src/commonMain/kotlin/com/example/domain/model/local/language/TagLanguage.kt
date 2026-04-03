package com.example.domain.model.local.language

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
            if (iso == SYSTEM.iso) return SYSTEM

            // Usando entries (moderno) em vez de values()
            entries.firstOrNull { it.iso == iso }?.let { return it }

            val normalized = iso.substringBefore("-")
            return entries.firstOrNull { it.iso == normalized } ?: SYSTEM
        }

        val listOfLanguages = listOf(
            ENGLISH.iso,
            GERMAN.iso,
            SPANISH.iso,
            FRENCH.iso,
            KOREAN.iso,
            PORTUGUESE.iso,
            RUSSIAN.iso,
            SYSTEM.iso,
        )
    }
}