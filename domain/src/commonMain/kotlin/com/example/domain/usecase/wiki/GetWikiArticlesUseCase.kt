package com.example.domain.usecase.wiki

import com.example.domain.models.wiki.WikiArticle
import com.example.domain.models.wiki.WikiCategory
import com.example.domain.repository.wiki.WikiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * UseCase responsável por buscar e filtrar artigos da Wiki.
 *
 * Aplica regras de negócio puras (ordenação, deduplicação) antes de
 * expor os dados à ViewModel — mantendo a lógica fora da UI e do repositório.
 */
class GetWikiArticlesUseCase(private val repository: WikiRepository) {

    /**
     * Retorna um [Flow] de artigos ordenados por relevância.
     * Artigos com bookmark aparecem primeiro; depois, ordem alfabética por título.
     */
    operator fun invoke(category: WikiCategory): Flow<Result<List<WikiArticle>>> =
        repository
            .getArticles(category)
            .map { articles ->
                val sorted = articles.sortedWith(
                    compareByDescending<WikiArticle> { it.isBookmarked }
                        .thenBy { it.title }
                )
                Result.success(sorted)
            }
            .catch { throwable ->
                emit(Result.failure(throwable))
            }
}

// ---------------------------------------------------------------------------

/**
 * UseCase de busca por texto livre.
 * Normaliza o query (trim, lowercase) e filtra localmente.
 */
class SearchWikiArticlesUseCase(private val repository: WikiRepository) {

    suspend operator fun invoke(rawQuery: String): Result<List<WikiArticle>> {
        val query = rawQuery.trim().lowercase()
        if (query.isBlank()) return Result.success(emptyList())

        return runCatching {
            repository.searchArticles(query)
        }
    }
}

// ---------------------------------------------------------------------------

/**
 * UseCase para alternar bookmark de um artigo.
 */
class ToggleBookmarkUseCase(private val repository: WikiRepository) {
    suspend operator fun invoke(articleId: String): Result<WikiArticle> =
        repository.toggleBookmark(articleId)
}