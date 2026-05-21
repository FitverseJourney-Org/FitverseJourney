package org.fitverse.domain.expect

expect class PlatformDate {
    val day: Int
    val month: Int       // 1-based: 1 = Janeiro, 12 = Dezembro
    val year: Int
    val epochMillis: Long
}

/**
 * Converte um timestamp Unix (ms) para [PlatformDate].
 * Implementado em cada plataforma usando a API de data nativa.
 */
expect fun Long.toPlatformDate(): PlatformDate