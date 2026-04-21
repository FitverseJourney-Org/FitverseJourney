package com.example.presentation.screens.ui.wiki

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.wiki.WikiCategory
import com.example.domain.model.wiki.WikiUiEvent
import com.example.domain.model.wiki.WikiUiState
import com.example.presentation.screens.ui.wiki.components.WikiArticleCard
import com.example.presentation.screens.ui.wiki.components.WikiCategoryChips
import com.example.presentation.screens.ui.wiki.components.WikiFeaturedCard
import com.example.presentation.screens.ui.wiki.components.WikiSearchBar
import com.example.presentation.screens.ui.wiki.components.WikiSkeletonCard
import com.example.presentation.screens.ui.wiki.viewmodel.WikiViewModel
import com.example.presentation.screens.widgets.FitVerseSpacer
import com.example.presentation.screens.widgets.FitverseTopAppBar

// ─────────────────────────────────────────────────────────────────────────────
// WikiScreen — Entry Point
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tela principal da Wiki Global do Fitverse Journey.
 *
 * Esta composable atua apenas como orquestradora de estado:
 * - Coleta o [WikiUiState] da ViewModel.
 * - Escuta [WikiUiEvent]s (efeitos colaterais).
 * - Delega a renderização para composables especializados.
 *
 * **Não contém lógica de negócio** — apenas mapeamento de estado → UI.
 *
 * @param viewModel       ViewModel injetada (via Koin/Hilt/manual).
 * @param onNavigateToArticle Callback de navegação para a tela de detalhe.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WikiScreen(
    viewModel: WikiViewModel,
    onNavigateToArticle: (String) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // ── Consumo de eventos pontuais (one-shot) ────────────────────────────
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is WikiUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is WikiUiEvent.NavigateToArticle -> onNavigateToArticle(event.articleId)
                is WikiUiEvent.XpEarned -> snackbarHostState.showSnackbar("+${event.xp} XP ganhos! 🎉")
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0,0,0,0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            FitverseTopAppBar(
                title = "WikiVerse",
                subtitle = {
                    Text(
                        text = "Conhecimento baseado em ciência 🔬",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        // ── Transição animada entre estados ──────────────────────────────
        AnimatedContent(
            // 1. Mudança crucial: anime baseado no "tipo" da classe, não no objeto em si.
            targetState = uiState::class,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "wiki_state_transition",
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        ) { _ -> // Ignoramos o estado recebido no lambda
            // 2. Lemos o uiState real diretamente aqui dentro
            when (val currentState = uiState) {
                is WikiUiState.Loading -> WikiLoadingContent()

                is WikiUiState.Success -> WikiSuccessContent(
                    state = currentState, // Passamos o currentState que tem a string atualizada
                    onCategorySelected = viewModel::onCategorySelected,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    onSearchActiveChanged = viewModel::onSearchActiveChanged, // Pode remover se não for usar mais
                    onArticleClick = viewModel::onArticleClicked,
                    onBookmarkToggle = viewModel::onBookmarkToggled,
                    onRefresh = viewModel::onRefresh,
                )

                is WikiUiState.Error -> WikiErrorContent(
                    message = currentState.message,
                    canRetry = currentState.canRetry,
                    onRetry = viewModel::onRetry,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// WikiSuccessContent — Conteúdo principal (estado Success)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WikiSuccessContent(
    state: WikiUiState.Success,
    onCategorySelected: (WikiCategory) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchActiveChanged: (Boolean) -> Unit,
    onArticleClick: (String) -> Unit,
    onBookmarkToggle: (String) -> Unit,
    onRefresh: () -> Unit,
) {
    val listState = rememberLazyListState()
    val pullRefreshState = rememberPullToRefreshState()


    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        state = pullRefreshState,
        indicator = {
            // Optional: Customize the indicator here if needed,
            // otherwise it uses the default M3 style automatically.
            PullToRefreshDefaults.Indicator(
                state = pullRefreshState,
                isRefreshing = state.isRefreshing,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    ){
        LazyColumn(
            state = listState,
        ) {
            // ── SearchBar ─────────────────────────────────────────────────
            item(key = "search_bar") {
                WikiSearchBar(
                    query = state.searchQuery,
                    onQueryChange = onSearchQueryChanged, // Apenas atualiza o texto
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                FitVerseSpacer(vertical = true, value = 10.dp)
            }

            // ── Category Chips ────────────────────────────────────────────
            item(key = "category_chips") {
                WikiCategoryChips(
                    categories = WikiCategory.entries,
                    selectedCategory = state.selectedCategory,
                    onCategoryClick = onCategorySelected,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                Spacer(Modifier.height(8.dp))
            }

            // ── Featured Card (apenas fora do modo busca e categoria "Todos") ──
            val featuredArticle = state.featuredArticle

            // 2. Perform the checks against the local copy
            if (state.searchQuery.isBlank() &&
                state.selectedCategory == WikiCategory.ALL &&
                featuredArticle != null
            ) {
                item(key = "featured_${featuredArticle.id}") {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically(),
                    ) {
                        WikiFeaturedCard(
                            article = featuredArticle, // Smart cast works here!
                            onClick = { onArticleClick(featuredArticle.id) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            // ── Seção de contagem ─────────────────────────────────────────
            item(key = "section_header") {
                val label = if (state.searchQuery.isNotBlank())
                    "🔍 ${state.articles.size} resultado(s) para \"${state.searchQuery}\""
                else
                    "${state.articles.size} artigos · ${state.selectedCategory.displayName}"

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }

            // ── Empty state ───────────────────────────────────────────────
            if (state.articles.isEmpty()) {
                item(key = "empty") {
                    EmptyContent(query = state.searchQuery)
                }
            }

            // ── Lista de cards ────────────────────────────────────────────
            items(items = state.articles,key = { it.id }) { article ->
                WikiArticleCard(
                    article = article,
                    onClick = { onArticleClick(article.id) },
                    onBookmark = { onBookmarkToggle(article.id) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .animateItem(), // Animação de inserção/remoção do M3
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Estados auxiliares (Loading, Error, Empty)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun WikiLoadingContent() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(5) { WikiSkeletonCard() }
    }
}

@Composable
private fun WikiErrorContent(
    message: String,
    canRetry: Boolean,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp),
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error,
            )
            Text(
                text = "Ops! Algo deu errado",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (canRetry) {
                Button(onClick = onRetry) {
                    Text("Tentar novamente")
                }
            }
        }
    }
}

@Composable
private fun EmptyContent(query: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = "🔍", style = MaterialTheme.typography.displaySmall)
            Text(
                text = if (query.isNotBlank()) "Sem resultados para \"$query\""
                else "Nenhum artigo disponível",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Tente outra busca ou selecione uma categoria diferente.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}