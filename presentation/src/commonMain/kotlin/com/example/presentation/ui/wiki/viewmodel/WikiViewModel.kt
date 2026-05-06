package com.example.presentation.ui.wiki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.wiki.WikiCategory
import com.example.domain.models.wiki.WikiUiEvent
import com.example.domain.models.wiki.WikiUiState
import com.example.domain.usecase.wiki.GetWikiArticlesUseCase
import com.example.domain.usecase.wiki.SearchWikiArticlesUseCase
import com.example.domain.usecase.wiki.ToggleBookmarkUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel da WikiScreen.
 *
 * Responsabilidades:
 * - Orquestrar os UseCases de domínio.
 * - Expor [uiState] como [StateFlow] imutável para a composable.
 * - Despachar [WikiUiEvent]s únicos (snackbar, navegação) via [Channel].
 * - Gerenciar debounce de busca para evitar requisições excessivas.
 *
 * Não contém lógica de UI nem referência a Context/View.
 */
@OptIn(FlowPreview::class)
class WikiViewModel(
    private val getWikiArticlesUseCase: GetWikiArticlesUseCase,
    private val searchWikiArticlesUseCase: SearchWikiArticlesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
) : ViewModel() {

    // ── State ─────────────────────────────────────────────────────────────────

    private val _uiState = MutableStateFlow<WikiUiState>(WikiUiState.Loading)
    val uiState: StateFlow<WikiUiState> = _uiState.asStateFlow()

    /** Canal de eventos pontuais (snackbar, navegação, XP). */
    private val _events = Channel<WikiUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    /** Flow interno de query de busca — com debounce aplicado. */
    private val _searchQuery = MutableStateFlow("")

    // ── Init ──────────────────────────────────────────────────────────────────

    init {
        loadArticles(WikiCategory.ALL)
        observeSearchQuery()
    }

    // ── Public Actions ────────────────────────────────────────────────────────

    /** Usuário selecionou uma categoria no chip selector. */
    fun onCategorySelected(category: WikiCategory) {
        // Limpa busca ao trocar de categoria
        _searchQuery.value = ""
        val currentState = _uiState.value
        if (currentState is WikiUiState.Success) {
            _uiState.update {
                (it as WikiUiState.Success).copy(
                    selectedCategory = category,
                    searchQuery = "",
                    isSearchActive = false,
                )
            }
        }
        loadArticles(category)
    }

    /** Usuário digitou na barra de pesquisa. */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (_uiState.value is WikiUiState.Success) {
            _uiState.update {
                (it as WikiUiState.Success).copy(searchQuery = query)
            }
        }
    }

    /** Usuário focou/desfocou a barra de pesquisa. */
    fun onSearchActiveChanged(isActive: Boolean) {
        if (_uiState.value is WikiUiState.Success) {
            _uiState.update {
                (it as WikiUiState.Success).copy(isSearchActive = isActive)
            }
        }
        // Ao fechar busca, limpa o query e reexibe lista da categoria
        if (!isActive) {
            _searchQuery.value = ""
            val currentCategory =
                (_uiState.value as? WikiUiState.Success)?.selectedCategory ?: WikiCategory.ALL
            loadArticles(currentCategory)
        }
    }

    /** Usuário puxou para atualizar (pull-to-refresh). */
    fun onRefresh() {
        val currentCategory =
            (_uiState.value as? WikiUiState.Success)?.selectedCategory ?: WikiCategory.ALL
        _uiState.update {
            if (it is WikiUiState.Success) it.copy(isRefreshing = true) else it
        }
        loadArticles(currentCategory)
    }

    /** Usuário clicou no ícone de bookmark. */
    fun onBookmarkToggled(articleId: String) {
        viewModelScope.launch {
            toggleBookmarkUseCase(articleId)
                .onSuccess { updatedArticle ->
                    val message = if (updatedArticle.isBookmarked) "Salvo nos favoritos ✨"
                    else "Removido dos favoritos"
                    _events.send(WikiUiEvent.ShowSnackbar(message))

                    // Atualiza o item na lista sem recarregar tudo
                    _uiState.update { state ->
                        if (state is WikiUiState.Success) {
                            state.copy(
                                articles = state.articles.map { article ->
                                    if (article.id == articleId) updatedArticle else article
                                },
                                allArticles = state.allArticles.map { article ->
                                    if (article.id == articleId) updatedArticle else article
                                },
                            )
                        } else state
                    }
                }
                .onFailure {
                    _events.send(WikiUiEvent.ShowSnackbar("Não foi possível salvar. Tente novamente."))
                }
        }
    }

    /** Usuário abriu um artigo — concede XP e navega. */
    fun onArticleClicked(articleId: String) {
        viewModelScope.launch {
            _events.send(WikiUiEvent.NavigateToArticle(articleId))
        }
    }

    /** Usuário clicou em "Tentar novamente" na tela de erro. */
    fun onRetry() {
        _uiState.value = WikiUiState.Loading
        loadArticles(WikiCategory.ALL)
    }

    // ── Private Helpers ───────────────────────────────────────────────────────

    private fun loadArticles(category: WikiCategory) {
        viewModelScope.launch {
            getWikiArticlesUseCase(category).collectLatest { result ->
                result
                    .onSuccess { articles ->
                        _uiState.update { currentState ->
                            WikiUiState.Success(
                                articles = articles,
                                allArticles = articles,
                                selectedCategory = category,
                                searchQuery = (currentState as? WikiUiState.Success)?.searchQuery ?: "",
                                isRefreshing = false,
                                featuredArticle = articles.firstOrNull(),
                            )
                        }
                    }
                    .onFailure { throwable ->
                        _uiState.value = WikiUiState.Error(
                            message = throwable.message ?: "Erro ao carregar conteúdo.",
                        )
                    }
            }
        }
    }

    /**
     * Observa o [_searchQuery] com debounce de 400ms para disparar a busca
     * apenas quando o usuário parar de digitar, evitando chamadas excessivas.
     */
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(400L)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        // Restaura lista da categoria sem refazer network call
                        val currentState = _uiState.value as? WikiUiState.Success ?: return@collect
                        _uiState.update {
                            (it as WikiUiState.Success).copy(articles = currentState.allArticles)
                        }
                        return@collect
                    }

                    searchWikiArticlesUseCase(query)
                        .onSuccess { results ->
                            _uiState.update {
                                if (it is WikiUiState.Success) it.copy(articles = results) else it
                            }
                        }
                        .onFailure {
                            _events.send(WikiUiEvent.ShowSnackbar("Erro na busca. Tente novamente."))
                        }
                }
        }
    }
}