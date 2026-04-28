package com.example.domain.usecase.wiki

import com.example.domain.models.wiki.WikiArticle
import com.example.domain.repository.wiki.WikiRepository

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