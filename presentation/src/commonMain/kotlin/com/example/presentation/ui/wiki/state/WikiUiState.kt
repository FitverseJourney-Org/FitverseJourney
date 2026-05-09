package com.example.presentation.ui.wiki.state

import com.example.domain.models.wiki.WikiArticle
import com.example.domain.models.wiki.WikiCategory

/**
 * Estado imutável que descreve completamente a UI da WikiScreen em qualquer momento.
 *
 * Utilizamos uma sealed class para que o Compose possa usar `when` exaustivo
 * sem precisar de um `else`, tornando refatorações mais seguras.
 */
sealed class WikiUiState {

    /** Primeiro carregamento — exibe Skeleton/Shimmer. */
    data object Loading : WikiUiState()

    /**
     * Dados carregados com sucesso.
     *
     * @param articles           Lista filtrada e ordenada de artigos exibidos.
     * @param allArticles        Lista completa (usada ao limpar filtros/busca).
     * @param selectedCategory   Categoria atualmente selecionada no chip.
     * @param searchQuery        Texto digitado na barra de pesquisa.
     * @param isSearchActive     Indica se a barra de busca está em foco/expandida.
     * @param isRefreshing       Pull-to-refresh em andamento (não oculta o conteúdo).
     * @param featuredArticle    Artigo destacado no topo (hero card), se houver.
     */
    data class Success(
        val articles: List<WikiArticle>,
        val allArticles: List<WikiArticle>,
        val selectedCategory: WikiCategory = WikiCategory.ALL,
        val searchQuery: String = "",
        val isSearchActive: Boolean = false,
        val isRefreshing: Boolean = false,
        val featuredArticle: WikiArticle? = null,
    ) : WikiUiState()

    /**
     * Erro de carregamento.
     *
     * @param message     Mensagem de erro legível para o usuário.
     * @param canRetry    Se verdadeiro, exibe botão "Tentar novamente".
     */
    data class Error(
        val message: String,
        val canRetry: Boolean = true,
    ) : WikiUiState()
}

/**
 * Eventos pontuais disparados pela ViewModel para a UI (efeitos colaterais).
 * Consumidos via `LaunchedEffect` + `Channel` para garantir entrega única.
 */
sealed class WikiUiEvent {
    data class ShowSnackbar(val message: String) : WikiUiEvent()
    data class NavigateToArticle(val articleId: String) : WikiUiEvent()
    data class XpEarned(val xp: Int) : WikiUiEvent()
}