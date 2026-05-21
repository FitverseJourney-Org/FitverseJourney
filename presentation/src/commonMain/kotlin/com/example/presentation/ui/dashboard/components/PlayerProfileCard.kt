package org.fitverse.presentation.ui.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.ui.dashboard.model.PlayerRankData
import org.fitverse.presentation.ui.dashboard.model.RankTierSpec
import org.fitverse.presentation.ui.dashboard.model.RankXpChip

// ── Main composable ───────────────────────────────────────────────────────────

@Composable
fun PlayerProfileCard(
    data: PlayerRankData = PlayerRankData.Preview,
    modifier: Modifier = Modifier,
) {
    val rank = data.currentRank
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(20.dp),
        colors   = CardDefaults.cardColors(containerColor = cs.surface),
        border   = BorderStroke(1.dp, Color(0xFF2A2A38)),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {

            // ── Header: badge + rank info ─────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                RankBadge(spec = rank)
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text          = "RANK ATUAL",
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Medium,
                        letterSpacing = 2.sp,
                        color         = FitColors.TextMuted,
                    )
                    Text(
                        text          = rank.name,
                        fontSize      = 30.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color         = Color.White,
                        lineHeight    = 32.sp,
                    )
                    Text(
                        text     = "${data.totalXp} XP TOTAL",
                        fontSize = 12.sp,
                        color    = FitColors.TextMuted,
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Rank progression tabs ─────────────────────────────────────────
            RankTabRow(currentSpec = rank)

            Spacer(Modifier.height(14.dp))

            // ── XP progress header ────────────────────────────────────────────
            if (!rank.isMaxRank && data.nextRank != null) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Próx: ", fontSize = 12.sp, color = FitColors.TextMuted)
                        Text(
                            text       = data.nextRank.name,
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = data.nextRank.accentColor,
                        )
                    }
                    Text(
                        text       = "${(data.progressFraction * 100).toInt()}%",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color      = rank.accentColor,
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF2A2A3A)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(data.progressFraction)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(rank.progressBarStart, rank.progressBarEnd)
                                )
                            ),
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text     = "faltam ${data.xpToNext} XP",
                    fontSize = 11.sp,
                    color    = FitColors.TextMuted,
                    modifier = Modifier.align(Alignment.End),
                )

                Spacer(Modifier.height(14.dp))
            } else {
                // Rank máximo — exibe badge de conquista
                Text(
                    text       = "RANK MÁXIMO ATINGIDO",
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color      = rank.accentColor,
                    modifier   = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(Modifier.height(14.dp))
            }

            // ── XP source chips ───────────────────────────────────────────────
            XpChipsGrid(chips = data.xpChips)
        }
    }
}

// ── RankBadge ─────────────────────────────────────────────────────────────────
//
// Círculo com ícone de medalha e número do tier no topo.
// Todas as cores vêm de [RankTierSpec], tornando o badge
// completamente data-driven e único por rank.

@Composable
private fun RankBadge(spec: RankTierSpec) {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(spec.badgeBg)
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(
                    listOf(spec.badgeBorderTop, spec.badgeBorderBottom)
                ),
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector        = Icons.Filled.MilitaryTech,
            contentDescription = "Rank ${spec.name}",
            tint               = spec.badgeIconTint,
            modifier           = Modifier.size(38.dp),
        )
        // Tier label pill
        Box(
            modifier = Modifier
                .size(25.dp)
                .clip(CircleShape)
                .background(spec.badgeNumberBg)
                .border(1.dp, spec.badgeNumberBorder, CircleShape)
                .align(Alignment.TopCenter)
                .offset(y = 2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text       = "${spec.tierLabel}",
                fontSize   = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White,
            )
        }
    }
}

// ── RankTabRow ────────────────────────────────────────────────────────────────

@Composable
private fun RankTabRow(currentSpec: RankTierSpec) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
    ) {
        RankTierSpec.all.forEach { spec ->
            val isPast    = spec.tier < currentSpec.tier
            val isCurrent = spec.tier == currentSpec.tier
            // Passados: cor própria do rank (desbloqueados individualmente)
            // Atual: branco
            // Futuros: muted/bloqueado
            val textColor = when {
                isCurrent -> Color.White
                isPast    -> spec.accentColor
                else      -> Color(0xFF7E7E95)
            }
            Column(
                modifier            = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Barra indicadora: sólida no atual, fina+alpha nos passados, invisível nos futuros
                when {
                    isCurrent -> Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(currentSpec.accentColor, RoundedCornerShape(35.dp))
                    )
                    isPast -> Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(spec.accentColor, RoundedCornerShape(35.dp))
                    )
                    else -> Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(Color(0xFF262525), RoundedCornerShape(35.dp))
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text          = spec.name,
                    fontSize      = 10.sp,
                    fontWeight    = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    letterSpacing = 0.5.sp,
                    color         = textColor,
                )
            }
        }
    }
}

// ── XP Chips grid ─────────────────────────────────────────────────────────────

@Composable
private fun XpChipsGrid(chips: List<RankXpChip>) {
    val rows = chips.chunked(4)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { rowChips ->
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                rowChips.forEach { chip ->
                    XpChipItem(chip = chip, modifier = Modifier.weight(1f))
                }
                repeat(4 - rowChips.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun XpChipItem(chip: RankXpChip, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(chip.bgColor)
            .border(1.dp, chip.borderColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector        = chip.icon,
            contentDescription = chip.label,
            tint               = chip.iconColor,
            modifier           = Modifier.size(14.dp),
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text       = chip.amount,
            fontSize   = 11.sp,
            fontWeight = FontWeight.Bold,
            color      = Color.White,
        )
    }
}
