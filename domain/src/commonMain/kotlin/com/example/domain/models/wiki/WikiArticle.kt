package com.example.domain.models.wiki

/**
 * Modelo de domínio que representa um artigo/pílula de conhecimento na Wiki Global.
 *
 * @param id          Identificador único do artigo.
 * @param title       Título principal exibido no card.
 * @param summary     Resumo curto (≤ 120 caracteres) exibido no card.
 * @param body        Conteúdo completo em Markdown, exibido na tela de detalhe.
 * @param category    Categoria à qual o artigo pertence.
 * @param imageUrl    URL da imagem de capa (opcional; usa placeholder quando nulo).
 * @param iconEmoji   Emoji representativo usado como fallback quando não há imagem.
 * @param tags        Lista de tags para refinamento de busca.
 * @param readingTimeMinutes Tempo estimado de leitura em minutos.
 * @param isBookmarked Indica se o usuário salvou este artigo.
 * @param xpReward    XP concedido ao usuário ao completar a leitura (gamificação).
 * @param sources     Referências científicas que embasam o conteúdo.
 */
data class WikiArticle(
    val id: String,
    val title: String,
    val summary: String,
    val body: String,
    val category: WikiCategory,
    val imageUrl: String? = null,
    val iconEmoji: String,
    val tags: List<String> = emptyList(),
    val readingTimeMinutes: Int = 3,
    val isBookmarked: Boolean = false,
    val xpReward: Int = 50,
    val sources: List<String> = emptyList(),
)