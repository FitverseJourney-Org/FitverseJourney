package com.example.expect


// ─────────────────────────────────────────────────────────────────────────────
// commonMain — contrato KMP-safe para datas
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Abstração de data KMP-safe usada em toda a camada de domínio.
 *
 * Usar `expect/actual` em vez de `java.util.Date` ou `kotlinx-datetime`
 * permite que cada plataforma use sua API nativa sem overhead de dependência:
 * - Android/JVM → `java.time.LocalDate`
 * - iOS         → `NSDateComponents`
 *
 * ## Campos expostos
 * - [day], [month], [year] → exibição na UI e filtros de período.
 * - [epochMillis] → armazenamento e ordenação no banco/rede (Long é serializable).
 */
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

