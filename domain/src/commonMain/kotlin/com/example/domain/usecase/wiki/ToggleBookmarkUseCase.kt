package com.example.domain.usecase.wiki

import com.example.domain.models.wiki.WikiArticle
import com.example.domain.repository.wiki.WikiRepository

/**
 * UseCase para alternar bookmark de um artigo.
 */

class ToggleBookmarkUseCase(private val repository: WikiRepository) {
    suspend operator fun invoke(articleId: String): Result<WikiArticle> =
        repository.toggleBookmark(articleId)
}