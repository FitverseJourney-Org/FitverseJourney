package org.fitverse.presentation.ui.dashboard.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// ── Rank Tier Specification ───────────────────────────────────────────────────
//
// Contém todas as especificações visuais e de progressão de cada rank.
// Escalonamento de XP:
//   BRONZE   :     0 – 4 999  (span  5 000)
//   PRATA    : 5 000 – 14 999 (span 10 000)
//   OURO     :15 000 – 29 999 (span 15 000)
//   PLATINA  :30 000 – 54 999 (span 25 000)
//   LENDÁRIO :55 000+         (rank máximo)
//
// Verificação com os dados da imagem de referência (9 000 XP em PRATA):
//   xpInRank  = 9 000 – 5 000       = 4 000
//   progress  = 4 000 / 10 000      = 40 %   ✓
//   xpToNext  = 15 000 – 9 000      = 6 000  ✓

@Immutable
data class RankTierSpec(
    /** Índice 0-based do rank (0 = Bronze … 4 = Lendário). */
    val tier: Int,
    /** Nome exibido em maiúsculas ("BRONZE", "PRATA" …). */
    val name: String,
    /** XP total mínimo para entrar neste rank. */
    val minTotalXp: Int,
    /** XP necessário para avançar ao próximo rank (0 = rank máximo). */
    val xpSpan: Int,

    // ── Cores do badge/avatar ─────────────────────────────────────────────
    val badgeBg: Color,
    val badgeBorderTop: Color,
    val badgeBorderBottom: Color,
    val badgeIconTint: Color,
    val badgeNumberBg: Color,
    val badgeNumberBorder: Color,

    // ── Cores de destaque (tabs + barra de progresso) ─────────────────────
    /** Cor primária do rank — usada na tab ativa e nos textos de destaque. */
    val accentColor: Color,
    val progressBarStart: Color,
    val progressBarEnd: Color,
) {
    /** `true` se este é o rank máximo (sem próximo rank). */
    val isMaxRank: Boolean get() = xpSpan == 0

    /** Rótulo numérico 1-based exibido no badge (BRONZE=1 … LENDÁRIO=5). */
    val tierLabel: Int get() = tier + 1

    companion object {
        val Bronze = RankTierSpec(
            tier = 0, name = "BRONZE",
            minTotalXp = 0, xpSpan = 5_000,
            badgeBg           = Color(0xFF1E1510),
            badgeBorderTop    = Color(0xFF8B5A2B),
            badgeBorderBottom = Color(0xFF4A2E10),
            badgeIconTint     = Color(0xFFCD7F32),
            badgeNumberBg     = Color(0xFF2A1C0F),
            badgeNumberBorder = Color(0xFF7A4A1F),
            accentColor       = Color(0xFFCD7F32),
            progressBarStart  = Color(0xFFCD7F32),
            progressBarEnd    = Color(0xFFE8A056),
        )

        val Prata = RankTierSpec(
            tier = 1, name = "PRATA",
            minTotalXp = 5_000, xpSpan = 10_000,
            badgeBg           = Color(0xFF222235),
            badgeBorderTop    = Color(0xFF6655BB),
            badgeBorderBottom = Color(0xFF332266),
            badgeIconTint     = Color(0xFFC0C0D0),
            badgeNumberBg     = Color(0xFF2E2E48),
            badgeNumberBorder = Color(0xFF5544AA),
            accentColor       = Color(0xFFEEEEFF),
            progressBarStart  = Color(0xFFBBBBCC),
            progressBarEnd    = Color(0xFFEEEEFF),
        )

        val Ouro = RankTierSpec(
            tier = 2, name = "OURO",
            minTotalXp = 15_000, xpSpan = 15_000,
            badgeBg           = Color(0xFF1E1800),
            badgeBorderTop    = Color(0xFFCCA300),
            badgeBorderBottom = Color(0xFF7A6200),
            badgeIconTint     = Color(0xFFFFD700),
            badgeNumberBg     = Color(0xFF2A2200),
            badgeNumberBorder = Color(0xFF887000),
            accentColor       = Color(0xFFFFD700),
            progressBarStart  = Color(0xFFFFD700),
            progressBarEnd    = Color(0xFFFFF0A0),
        )

        val Platina = RankTierSpec(
            tier = 3, name = "PLATINA",
            minTotalXp = 30_000, xpSpan = 25_000,
            badgeBg           = Color(0xFF0A2020),
            badgeBorderTop    = Color(0xFF00D4D4),
            badgeBorderBottom = Color(0xFF006868),
            badgeIconTint     = Color(0xFF00E8E8),
            badgeNumberBg     = Color(0xFF0C2828),
            badgeNumberBorder = Color(0xFF1A6060),
            accentColor       = Color(0xFF00D4D4),
            progressBarStart  = Color(0xFF00D4B4),
            progressBarEnd    = Color(0xFF80FFEE),
        )

        val Lendario = RankTierSpec(
            tier = 4, name = "LENDÁRIO",
            minTotalXp = 55_000, xpSpan = 0,
            badgeBg           = Color(0xFF1A0A28),
            badgeBorderTop    = Color(0xFF9D6FFF),
            badgeBorderBottom = Color(0xFF4A1FA0),
            badgeIconTint     = Color(0xFFCF9FFF),
            badgeNumberBg     = Color(0xFF220F38),
            badgeNumberBorder = Color(0xFF6A2FAA),
            accentColor       = Color(0xFF9D6FFF),
            progressBarStart  = Color(0xFF9D6FFF),
            progressBarEnd    = Color(0xFFFF6B9D),
        )

        /** Lista ordenada de todos os ranks — índice == tier. */
        val all: List<RankTierSpec> = listOf(Bronze, Prata, Ouro, Platina, Lendario)

        /** Retorna o rank correspondente ao XP total do jogador. */
        fun forTotalXp(totalXp: Int): RankTierSpec =
            all.lastOrNull { totalXp >= it.minTotalXp } ?: Bronze
    }
}

// ── XP Chip ───────────────────────────────────────────────────────────────────

@Immutable
data class RankXpChip(
    val icon: ImageVector,
    val label: String,
    val amount: String,
    val bgColor: Color,
    val borderColor: Color,
    val iconColor: Color,
)

// ── Player Rank Data ──────────────────────────────────────────────────────────

@Stable
data class PlayerRankData(
    val totalXp: Int,
    val currentRank: RankTierSpec,
    /** Rank seguinte; `null` se o jogador está no rank máximo. */
    val nextRank: RankTierSpec?,
    /** Progresso dentro do rank atual: 0f = início, 1f = completo. */
    val progressFraction: Float,
    /** XP restante para atingir o próximo rank (0 se rank máximo). */
    val xpToNext: Int,
    /** Chips de fontes de XP exibidos na parte inferior do card. */
    val xpChips: List<RankXpChip> = defaultXpChips,
) {
    companion object {
        val defaultXpChips: List<RankXpChip> = listOf(
            RankXpChip(
                icon        = Icons.Filled.EmojiEvents,
                label       = "Conquistas",
                amount      = "+840",
                bgColor     = Color(0xFF162A1D),
                borderColor = Color(0xFF255235),
                iconColor   = Color(0xFFFFD000),
            ),
            RankXpChip(
                icon        = Icons.Filled.Assignment,
                label       = "Missões",
                amount      = "+1240",
                bgColor     = Color(0xFF141430),
                borderColor = Color(0xFF252580),
                iconColor   = Color(0xFF6699FF),
            ),
            RankXpChip(
                icon        = Icons.Filled.LocalFireDepartment,
                label       = "Sequência",
                amount      = "+1400",
                bgColor     = Color(0xFF3A1210),
                borderColor = Color(0xFF6A2218),
                iconColor   = Color(0xFFFF5533),
            ),
            RankXpChip(
                icon        = Icons.Filled.Diamond,
                label       = "Desafios",
                amount      = "+520",
                bgColor     = Color(0xFF0C2828),
                borderColor = Color(0xFF1A4848),
                iconColor   = Color(0xFF55DDDD),
            ),
            RankXpChip(
                icon        = Icons.Filled.Bolt,
                label       = "Bônus",
                amount      = "+460",
                bgColor     = Color(0xFF252200),
                borderColor = Color(0xFF443C00),
                iconColor   = Color(0xFFFFDD00),
            ),
        )

        /**
         * Constrói [PlayerRankData] a partir do XP total do jogador.
         * Todos os campos derivados (rank atual, próximo, progresso, XP restante)
         * são calculados automaticamente com base em [RankTierSpec.all].
         */
        fun fromTotalXp(
            totalXp: Int,
            xpChips: List<RankXpChip> = defaultXpChips,
        ): PlayerRankData {
            val current  = RankTierSpec.forTotalXp(totalXp)
            val next     = RankTierSpec.all.getOrNull(current.tier + 1)
            val xpInRank = totalXp - current.minTotalXp
            val progress = if (current.xpSpan > 0)
                (xpInRank.toFloat() / current.xpSpan).coerceIn(0f, 1f)
            else 1f
            val xpToNext = if (next != null) (current.minTotalXp + current.xpSpan) - totalXp else 0
            return PlayerRankData(
                totalXp          = totalXp,
                currentRank      = current,
                nextRank         = next,
                progressFraction = progress,
                xpToNext         = xpToNext,
                xpChips          = xpChips,
            )
        }

        /** Estado de preview com os valores exatos da imagem de referência. */
        val Preview: PlayerRankData = fromTotalXp(59_000)
    }
}
