package org.fitverse.domain.usecase.wiki

import org.fitverse.domain.models.wiki.WikiArticle
import org.fitverse.domain.models.wiki.WikiCategory
import org.fitverse.domain.repository.wiki.WikiRepository
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
