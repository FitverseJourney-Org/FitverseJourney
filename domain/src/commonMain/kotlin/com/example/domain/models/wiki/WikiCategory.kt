package org.fitverse.domain.models.wiki

/**
 * Representa as categorias disponíveis na Wiki Global.
 * Cada categoria possui um rótulo de exibição e um ícone associado.
 */
enum class WikiCategory(
    val displayName: String,
    val emoji: String,
) {
    ALL(displayName = "Todos", emoji = "🌐"),
    HYPERTROPHY(displayName = "Hipertrofia", emoji = "💪"),
    NUTRITION(displayName = "Alimentação", emoji = "🥗"),
    SUPPLEMENTS(displayName = "Suplementos", emoji = "🧪"),
    TIPS(displayName = "Dicas", emoji = "💡"),
}