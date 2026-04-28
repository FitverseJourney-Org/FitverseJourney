package com.example.domain.repository.wiki

import com.example.domain.models.wiki.WikiArticle
import com.example.domain.models.wiki.WikiCategory
import kotlinx.coroutines.flow.Flow

/**
 * Contrato do repositório da Wiki Global.
 * A camada de domínio depende desta interface; as implementações
 * concretas ficam na camada de dados (data layer).
 */
interface WikiRepository {

    /**
     * Retorna todos os artigos, opcionalmente filtrados por categoria.
     * Emite atualizações em tempo real (ex.: cache local → remoto).
     */
    fun getArticles(category: WikiCategory = WikiCategory.ALL): Flow<List<WikiArticle>>

    /**
     * Busca artigos por termo livre nos campos título, resumo e tags.
     */
    suspend fun searchArticles(query: String): List<WikiArticle>

    /**
     * Alterna o estado de favorito de um artigo e persiste localmente.
     */
    suspend fun toggleBookmark(articleId: String): Result<WikiArticle>

    /**
     * Marca o artigo como lido e concede XP ao usuário (gamificação).
     */
    suspend fun markAsRead(articleId: String): Result<Int>
}