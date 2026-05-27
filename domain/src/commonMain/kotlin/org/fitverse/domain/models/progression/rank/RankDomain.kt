package org.fitverse.domain.models.progression.rank

/**
 * Dados de gamificação do usuário: rank atual, XP e distância para o próximo nível.
 *
 * Produzido pelo [GetRankInfoUseCase] e mapeado pela ViewModel para [RankInfo] (UI).
 *
 * @param rank       Nome do rank atual (ex.: "PRATA", "OURO").
 * @param currentXp  XP acumulado no rank atual.
 * @param nextRank   Nome do próximo rank.
 * @param xpToNext   XP restante para avançar de nível.
 */
data class RankDomain(
    val rank      : String,
    val currentXp : Int,
    val nextRank  : String,
    val xpToNext  : Int,
)
