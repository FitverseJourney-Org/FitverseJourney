package org.fitverse.domain.usecase.wiki

import org.fitverse.domain.models.wiki.WikiArticle
import org.fitverse.domain.repository.wiki.WikiRepository

/**
 * UseCase para alternar bookmark de um artigo.
 */

class ToggleBookmarkUseCase(private val repository: WikiRepository) {
    suspend operator fun invoke(articleId: String): Result<WikiArticle> =
        repository.toggleBookmark(articleId)
}