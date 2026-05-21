package org.fitverse.data.repository

import org.fitverse.domain.models.wiki.WikiArticle
import org.fitverse.domain.models.wiki.WikiCategory
import org.fitverse.domain.repository.wiki.WikiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Implementação concreta do [WikiRepository].
 *
 * Em produção, esta classe faria chamadas a uma API REST/GraphQL e
 * persistiria os resultados em um banco local (Room/SQLDelight).
 *
 * Para desenvolvimento/preview, utiliza dados em memória com
 * um delay artificial para simular latência de rede.
 */
class WikiRepositoryImpl : WikiRepository {

    /** Fonte de verdade local — permite atualização reativa de bookmarks. */
    private val _articlesFlow = MutableStateFlow(FAKE_ARTICLES)

    override fun getArticles(category: WikiCategory): Flow<List<WikiArticle>> =
        _articlesFlow.map { articles ->
            if (category == WikiCategory.ALL) articles
            else articles.filter { it.category == category }
        }

    override suspend fun searchArticles(query: String): List<WikiArticle> {
        delay(300) // simula latência
        val normalizedQuery = query.trim().lowercase()
        return _articlesFlow.value.filter { article ->
            article.title.lowercase().contains(normalizedQuery) ||
                    article.summary.lowercase().contains(normalizedQuery) ||
                    article.tags.any { it.lowercase().contains(normalizedQuery) }
        }
    }

    override suspend fun toggleBookmark(articleId: String): Result<WikiArticle> {
        val updated = _articlesFlow.value.map { article ->
            if (article.id == articleId) article.copy(isBookmarked = !article.isBookmarked)
            else article
        }
        _articlesFlow.value = updated
        val target = updated.find { it.id == articleId }
            ?: return Result.failure(NoSuchElementException("Artigo não encontrado: $articleId"))
        return Result.success(target)
    }

    override suspend fun markAsRead(articleId: String): Result<Int> {
        val article = _articlesFlow.value.find { it.id == articleId }
            ?: return Result.failure(NoSuchElementException("Artigo não encontrado: $articleId"))
        return Result.success(article.xpReward)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Dados fake para desenvolvimento e previews do Compose
// ─────────────────────────────────────────────────────────────────────────────

val FAKE_ARTICLES = listOf(
    WikiArticle(
        id = "art_001",
        title = "Princípios da Hipertrofia Muscular",
        summary = "Entenda os mecanismos científicos por trás do crescimento muscular: tensão mecânica, estresse metabólico e dano muscular.",
        body = "## O que é Hipertrofia?\n\nA hipertrofia muscular é o aumento do volume...",
        category = WikiCategory.HYPERTROPHY,
        iconEmoji = "💪",
        tags = listOf("hipertrofia", "músculo", "treino", "ciência"),
        readingTimeMinutes = 5,
        xpReward = 75,
        sources = listOf("Schoenfeld, B.J. (2010). J Strength Cond Res"),
    ),
    WikiArticle(
        id = "art_002",
        title = "Creatina: O Suplemento Mais Estudado",
        summary = "Creatina monoidratada aumenta a ressíntese de ATP, melhora a performance em exercícios de alta intensidade e é segura a longo prazo.",
        body = "## O que é Creatina?\n\nA creatina é um composto nitrogenado...",
        category = WikiCategory.SUPPLEMENTS,
        iconEmoji = "🧪",
        tags = listOf("creatina", "suplemento", "ATP", "performance"),
        readingTimeMinutes = 4,
        xpReward = 60,
        sources = listOf("Rawson & Volek (2003). J Strength Cond Res"),
    ),
    WikiArticle(
        id = "art_003",
        title = "Proteínas: Quantidade Ideal para Hipertrofia",
        summary = "A meta de 1,6 a 2,2g/kg de peso corporal por dia maximiza a síntese proteica muscular segundo meta-análises recentes.",
        body = "## Qual a quantidade ideal de proteína?\n\nPor muito tempo...",
        category = WikiCategory.NUTRITION,
        iconEmoji = "🥩",
        tags = listOf("proteína", "whey", "nutrição", "macros"),
        readingTimeMinutes = 3,
        xpReward = 50,
        sources = listOf("Morton et al. (2018). Br J Sports Med"),
    ),
    WikiArticle(
        id = "art_004",
        title = "Divisão de Treino ABCD: Guia Completo",
        summary = "Aprenda a estruturar sua semana de treinos para maximizar a frequência de cada grupo muscular sem comprometer a recuperação.",
        body = "## O que é a Divisão ABCD?\n\nA divisão ABCD é uma estratégia...",
        category = WikiCategory.HYPERTROPHY,
        iconEmoji = "📋",
        tags = listOf("divisão", "ABCD", "treino", "frequência", "volume"),
        readingTimeMinutes = 6,
        xpReward = 80,
    ),
    WikiArticle(
        id = "art_005",
        title = "EAAs vs. BCAAs: Qual Escolher?",
        summary = "Aminoácidos essenciais (EAAs) fornecem todos os aminoácidos necessários para síntese proteica, sendo superiores aos BCAAs isolados.",
        body = "## A diferença entre EAAs e BCAAs\n\nBCAAs (leucina, isoleucina e valina)...",
        category = WikiCategory.SUPPLEMENTS,
        iconEmoji = "⚗️",
        tags = listOf("EAA", "BCAA", "aminoácidos", "suplemento"),
        readingTimeMinutes = 4,
        xpReward = 65,
        sources = listOf("Wolfe, R.R. (2017). J Int Soc Sports Nutr"),
    ),
    WikiArticle(
        id = "art_006",
        title = "Sono e Recuperação Muscular",
        summary = "Dormir 7 a 9 horas por noite é essencial para a liberação de GH e a síntese proteica muscular. A privação de sono reduz ganhos em até 60%.",
        body = "## Por que o sono é tão importante?\n\nDurante o sono profundo (fase NREM)...",
        category = WikiCategory.TIPS,
        iconEmoji = "😴",
        tags = listOf("sono", "recuperação", "GH", "descanso"),
        readingTimeMinutes = 3,
        xpReward = 50,
    ),
    WikiArticle(
        id = "art_007",
        title = "Carboidratos no Pré-Treino",
        summary = "Consumir 30 a 60g de carboidratos de baixo a médio IG 60 minutos antes do treino melhora a performance e adia a fadiga.",
        body = "## Qual o papel dos carboidratos antes do treino?\n\nOs carboidratos são...",
        category = WikiCategory.NUTRITION,
        iconEmoji = "🍌",
        tags = listOf("carboidrato", "pré-treino", "energia", "glicogênio"),
        readingTimeMinutes = 3,
        xpReward = 45,
    ),
    WikiArticle(
        id = "art_008",
        title = "Princípio da Sobrecarga Progressiva",
        summary = "Aumentar gradualmente o estímulo (peso, volume ou densidade) ao longo do tempo é o principal driver do ganho de força e massa muscular.",
        body = "## O que é Sobrecarga Progressiva?\n\nO princípio da sobrecarga progressiva...",
        category = WikiCategory.HYPERTROPHY,
        iconEmoji = "📈",
        tags = listOf("progressão", "sobrecarga", "força", "volume"),
        readingTimeMinutes = 4,
        xpReward = 70,
    ),
)
