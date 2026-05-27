@file:OptIn(ExperimentalMaterial3Api::class)

package org.fitverse.presentation.ui.referrals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.widgets.FVCard
import org.fitverse.presentation.widgets.FVScreenHeader
import org.fitverse.presentation.widgets.FitverseTopAppBar

private val Teal get() = FitColors.Teal

private data class Milestone(val count: Int, val reward: String, val reached: Boolean)

private data class Friend(
    val initials     : String,
    val name         : String,
    val status       : FriendStatus,
    val avatarColor  : Color,
)

private enum class FriendStatus { ATIVO, PENDENTE, INATIVO }

private val milestones = listOf(
    Milestone(1,  "+7 dias",  reached = true),
    Milestone(3,  "+1 mês",   reached = true),
    Milestone(5,  "+2 meses", reached = false),
    Milestone(10, "+1 ano",   reached = false),
)

private val friends = listOf(
    Friend("LM", "Lucas Mendes",    FriendStatus.ATIVO,    Color(0xFF5B5EFF)),
    Friend("JS", "Juliana Silva",   FriendStatus.ATIVO,    Teal),
    Friend("CA", "Carlos Augusto",  FriendStatus.PENDENTE, Color(0xFFFF5C3D)),
    Friend("MF", "Maria Fernanda",  FriendStatus.INATIVO,  Color(0xFFFFB830)),
)

private const val currentReferrals = 3

// ═════════════════════════════════════════════════════════════════════════════
// Root
// ═════════════════════════════════════════════════════════════════════════════

@Composable
fun ReferralsScreen(
    onBack   : () -> Unit,
    modifier : Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            FitverseTopAppBar(
                modifierColumn = Modifier.statusBarsPadding(),
                title = "INDICAÇÕES",
                subtitle = {
                    Text(
                        text = "Convide amigos, ganhe PRO",
                        color = FitColors.TextMuted,
                        fontSize = 13.sp,
                    )
                },
                onBack = onBack,
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(FitColors.Bg)
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HeroCard()
            MilestoneCard()
            StatsRow()
            FriendsCard()
            HowItWorksCard()
            Spacer(Modifier.height(32.dp))
        }
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// Hero Card — referral code + action buttons
// ═════════════════════════════════════════════════════════════════════════════

@Composable
private fun HeroCard() {
    FVCard(modifier = Modifier.fillMaxWidth(), glowColor = Teal) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Teal.copy(alpha = 0.18f), Color.Transparent),
                            center = Offset(size.width / 2, 0f),
                            radius = size.width * 0.75f,
                        ),
                    )
                },
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text          = "SEU CÓDIGO DE INDICAÇÃO",
                    color         = FitColors.TextMuted,
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Teal.copy(alpha = 0.07f))
                        .border(1.5.dp, Teal.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text          = "FIT-RAFA2025",
                        color         = Teal,
                        fontSize      = 28.sp,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 4.sp,
                    )
                }
                Text(
                    text       = "Cada amigo que se cadastrar com seu código te dá dias PRO grátis.",
                    color      = FitColors.TextMuted,
                    fontSize   = 13.sp,
                    lineHeight = 19.sp,
                )
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    ActionButton(
                        icon     = Icons.Rounded.ContentCopy,
                        label    = "COPIAR",
                        accent   = Teal,
                        filled   = false,
                        modifier = Modifier.weight(1f),
                        onClick  = {},
                    )
                    ActionButton(
                        icon     = Icons.Rounded.Share,
                        label    = "COMPARTILHAR",
                        accent   = FitColors.Accent,
                        filled   = true,
                        modifier = Modifier.weight(1f),
                        onClick  = {},
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon     : ImageVector,
    label    : String,
    accent   : Color,
    filled   : Boolean,
    modifier : Modifier,
    onClick  : () -> Unit,
) {
    val bg = if (filled)
        Brush.horizontalGradient(listOf(accent, accent.copy(alpha = 0.80f)))
    else
        Brush.horizontalGradient(listOf(accent.copy(alpha = 0.10f), accent.copy(alpha = 0.10f)))

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .border(1.dp, if (filled) Color.Transparent else accent.copy(alpha = 0.40f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = if (filled) Color.Black else accent,
                modifier           = Modifier.size(16.dp),
            )
            Text(
                text          = label,
                color         = if (filled) Color.Black else accent,
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Black,
                letterSpacing = 0.8.sp,
            )
        }
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// Milestone Track Card
// ═════════════════════════════════════════════════════════════════════════════

@Composable
private fun MilestoneCard() {
    FVCard(modifier = Modifier.fillMaxWidth(), glowColor = Teal) {
        Column(modifier = Modifier.padding(16.dp),verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text(
                    text          = "TRILHA DE RECOMPENSAS",
                    color         = FitColors.TextMuted,
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Teal.copy(alpha = 0.10f))
                        .border(1.dp, Teal.copy(alpha = 0.30f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                ) {
                    Text(
                        text       = "$currentReferrals indicações",
                        color      = Teal,
                        fontSize   = 10.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            MilestoneTrack()
        }
    }
}

@Composable
private fun MilestoneTrack() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Row 1: circles connected by proportional lines
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            milestones.forEachIndexed { idx, milestone ->
                if (idx > 0) {
                    val gap = (milestone.count - milestones[idx - 1].count).toFloat()
                    Box(
                        modifier = Modifier
                            .weight(gap)
                            .height(2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(if (milestone.reached) Teal.copy(alpha = 0.60f) else FitColors.Outline),
                    )
                }
                MilestoneNode(milestone)
            }
        }
        // Row 2: labels aligned with the circles above (same weight/width structure)
        Row(modifier = Modifier.fillMaxWidth()) {
            milestones.forEachIndexed { idx, milestone ->
                if (idx > 0) {
                    val gap = (milestone.count - milestones[idx - 1].count).toFloat()
                    Spacer(Modifier.weight(gap))
                }
                Column(
                    modifier            = Modifier.width(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text          = "${milestone.count}",
                        color         = if (milestone.reached) Teal else FitColors.TextDisabled,
                        fontSize      = 8.sp,
                        fontWeight    = FontWeight.Black,
                        textAlign     = TextAlign.Center,
                        modifier      = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text          = milestone.reward,
                        color         = if (milestone.reached) FitColors.TextPrimary else FitColors.TextDisabled,
                        fontSize      = 8.sp,
                        fontWeight    = FontWeight.Bold,
                        textAlign     = TextAlign.Center,
                        modifier      = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun MilestoneNode(milestone: Milestone) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(if (milestone.reached) Teal.copy(alpha = 0.15f) else FitColors.Surface3)
            .border(1.5.dp, if (milestone.reached) Teal else FitColors.Outline, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (milestone.reached) {
            Icon(
                imageVector        = Icons.Rounded.Check,
                contentDescription = null,
                tint               = Teal,
                modifier           = Modifier.size(14.dp),
            )
        } else {
            Text(
                text       = "${milestone.count}",
                color      = FitColors.TextDisabled,
                fontSize   = 9.sp,
                fontWeight = FontWeight.Black,
            )
        }
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// Stats Row
// ═════════════════════════════════════════════════════════════════════════════

@Composable
private fun StatsRow() {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        StatCard(value = "3",   label = "TOTAL",  accent = Teal,             modifier = Modifier.weight(1f))
        StatCard(value = "2",   label = "ATIVOS",  accent = FitColors.Accent, modifier = Modifier.weight(1f))
        StatCard(value = "38d", label = "GANHOS",  accent = FitColors.Amber,  modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(value: String, label: String, accent: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(FitColors.Surface)
            .border(1.dp, accent.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(accent.copy(alpha = 0.08f), Color.Transparent),
                        startY = 0f,
                        endY   = size.height * 0.6f,
                    ),
                )
            }
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(text = value, color = accent, fontSize = 22.sp, fontWeight = FontWeight.Black)
        Text(
            text          = label,
            color         = FitColors.TextDisabled,
            fontSize      = 9.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = 0.8.sp,
        )
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// Friends List Card
// ═════════════════════════════════════════════════════════════════════════════

@Composable
private fun FriendsCard() {
    FVCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp),verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text          = "AMIGOS INDICADOS",
                color         = FitColors.TextMuted,
                fontSize      = 10.sp,
                fontWeight    = FontWeight.Black,
                letterSpacing = 1.5.sp,
            )
            friends.forEach { FriendRow(it) }
        }
    }
}

@Composable
private fun FriendRow(friend: Friend) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(friend.avatarColor.copy(alpha = 0.15f))
                .border(1.dp, friend.avatarColor.copy(alpha = 0.40f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = friend.initials, color = friend.avatarColor, fontSize = 13.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier            = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(text = friend.name, color = FitColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text     = when (friend.status) {
                    FriendStatus.ATIVO    -> "Cadastrado e ativo"
                    FriendStatus.PENDENTE -> "Aguardando cadastro"
                    FriendStatus.INATIVO  -> "Sem atividade recente"
                },
                color    = FitColors.TextDisabled,
                fontSize = 11.sp,
            )
        }
        StatusChip(friend.status)
    }
}

@Composable
private fun StatusChip(status: FriendStatus) {
    val (label, color) = when (status) {
        FriendStatus.ATIVO    -> "ATIVO"    to FitColors.Accent
        FriendStatus.PENDENTE -> "PENDENTE" to FitColors.Amber
        FriendStatus.INATIVO  -> "INATIVO"  to FitColors.TextDisabled
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.10f))
            .border(1.dp, color.copy(alpha = 0.30f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(text = label, color = color, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// How It Works Card
// ═════════════════════════════════════════════════════════════════════════════

@Composable
private fun HowItWorksCard() {
    FVCard(modifier = Modifier.fillMaxWidth(), glowColor = FitColors.Accent) {
        Column(modifier = Modifier.padding(16.dp),verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text          = "COMO FUNCIONA",
                color         = FitColors.TextMuted,
                fontSize      = 10.sp,
                fontWeight    = FontWeight.Black,
                letterSpacing = 1.5.sp,
            )
            StepItem(
                step  = 1,
                title = "Compartilhe seu código",
                desc  = "Envie seu código para amigos via WhatsApp, Instagram ou direct.",
            )
            StepItem(
                step  = 2,
                title = "Amigo se cadastra",
                desc  = "Seu amigo baixa o Fitverse e insere seu código no cadastro.",
            )
            StepItem(
                step  = 3,
                title = "Você ganha dias PRO",
                desc  = "Assim que ele ativar a conta, seus dias PRO são creditados automaticamente.",
            )
        }
    }
}

@Composable
private fun StepItem(step: Int, title: String, desc: String) {
    Row(
        verticalAlignment     = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(FitColors.Accent.copy(alpha = 0.12f))
                .border(1.dp, FitColors.Accent.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text       = "$step",
                color      = FitColors.Accent,
                fontSize   = 13.sp,
                fontWeight = FontWeight.Black,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(text = title, color = FitColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = desc,  color = FitColors.TextMuted,   fontSize = 12.sp, lineHeight = 17.sp)
        }
    }
}
