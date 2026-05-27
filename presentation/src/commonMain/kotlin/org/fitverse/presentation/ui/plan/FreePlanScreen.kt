@file:OptIn(ExperimentalMaterial3Api::class)

package org.fitverse.presentation.ui.plan

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.widgets.FVCard
import org.fitverse.presentation.widgets.FVScreenHeader
import org.fitverse.presentation.widgets.FitverseTopAppBar

private val Lime      get() = FitColors.Accent
private val Amber     get() = FitColors.Amber
private val Aqua      get() = FitColors.Secondary   // #00FFB2 — trial color
private val Gold            = Color(0xFFFFD700)

// ═════════════════════════════════════════════════════════════════════════════
// Root
// ═════════════════════════════════════════════════════════════════════════════

@Composable
fun FreePlanScreen(
    onBack   : () -> Unit,
    onTrial  : () -> Unit = {},
    modifier : Modifier = Modifier,
) {
    var isAnnual by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            FitverseTopAppBar(
                modifierColumn = Modifier.statusBarsPadding(),
                title = "PLANO",
                subtitle = {
                  Text(
                      text     = "Gerencie sua assinatura",
                      color    = FitColors.TextMuted,
                      fontSize = 13.sp,
                  )
                },
                onBack = onBack,
            )
        }
    ){
        Column(
            modifier = Modifier.fillMaxSize()
                .background(FitColors.Bg)
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HeroCard()
            TrialOfferCard(onTrial = onTrial)
            FeatureTableCard()
            PricingCard(isAnnual = isAnnual, onToggle = { isAnnual = !isAnnual })
            Spacer(Modifier.height(32.dp))
        }
    }

}

// ═════════════════════════════════════════════════════════════════════════════
// Hero Card
// ═════════════════════════════════════════════════════════════════════════════

@Composable
private fun HeroCard() {
    FVCard(modifier = Modifier.fillMaxWidth(), glowColor = Lime) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    // lime glow top-right
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Lime.copy(alpha = 0.16f), Color.Transparent),
                            center = Offset(size.width, 0f),
                            radius = size.width * 0.68f,
                        ),
                    )
                    // amber glow bottom-left
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Amber.copy(alpha = 0.10f), Color.Transparent),
                            center = Offset(0f, size.height),
                            radius = size.width * 0.58f,
                        ),
                    )
                }.padding(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    PlanBadge(label = "GRÁTIS", color = Lime)
                    ActivePill()
                }
                Text(
                    text          = "Plano Gratuito",
                    color         = FitColors.TextPrimary,
                    fontSize      = 24.sp,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = 0.3.sp,
                )
                Text(
                    text       = "Acesso às funcionalidades essenciais do Fitverse.",
                    color      = FitColors.TextMuted,
                    fontSize   = 13.sp,
                    lineHeight = 19.sp,
                )
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    QuickStat(label = "127",  sub = "treinos",     modifier = Modifier.weight(1f))
                    QuickStat(label = "23",   sub = "dias streak", modifier = Modifier.weight(1f))
                    QuickStat(label = "4 m",  sub = "ativo",       modifier = Modifier.weight(1f))
                }
                GradientCta(text = "FAZER UPGRADE PARA PRO", colors = listOf(Lime, Color(0xFF96CC00)))
            }
        }
    }
}

@Composable
private fun PlanBadge(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.40f), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text          = label,
            color         = color,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = 1.2.sp,
        )
    }
}

@Composable
private fun ActivePill() {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(Lime),
        )
        Text(
            text          = "PLANO ATIVO",
            color         = FitColors.TextMuted,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Medium,
            letterSpacing = 0.8.sp,
        )
    }
}

@Composable
private fun QuickStat(label: String, sub: String, modifier: Modifier) {
    Column(
        modifier            = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(FitColors.Surface3)
            .border(1.dp, FitColors.Border, RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(text = label, color = FitColors.TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
        Text(text = sub,   color = FitColors.TextDisabled, fontSize = 10.sp)
    }
}

@Composable
private fun GradientCta(text: String, colors: List<Color>, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.horizontalGradient(colors))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector        = Icons.Rounded.Star,
                contentDescription = null,
                tint               = Color.Black,
                modifier           = Modifier.size(18.dp),
            )
            Text(
                text          = text,
                color         = Color.Black,
                fontSize      = 13.sp,
                fontWeight    = FontWeight.Black,
                letterSpacing = 0.8.sp,
            )
        }
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// Trial Offer Card
// ═════════════════════════════════════════════════════════════════════════════

private val trialPerks = listOf(
    "IA Personal Trainer",
    "Treinos ilimitados",
    "Plano alimentar",
    "Sem anúncios",
)

@Composable
private fun TrialOfferCard(onTrial: () -> Unit) {
    FVCard(modifier = Modifier.fillMaxWidth(), glowColor = Aqua) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    // aqua glow — top-left
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Aqua.copy(alpha = 0.18f), Color.Transparent),
                            center = Offset(0f, 0f),
                            radius = size.width * 0.70f,
                        ),
                    )
                    // subtle lime glow — bottom-right
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Lime.copy(alpha = 0.08f), Color.Transparent),
                            center = Offset(size.width, size.height),
                            radius = size.width * 0.50f,
                        ),
                    )
                }.padding(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Header row
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Aqua.copy(alpha = 0.12f))
                            .border(1.dp, Aqua.copy(alpha = 0.40f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text          = "TRIAL GRATUITO",
                            color         = Aqua,
                            fontSize      = 10.sp,
                            fontWeight    = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                        )
                    }
                    Text(
                        text     = "Oferta limitada",
                        color    = FitColors.TextDisabled,
                        fontSize = 10.sp,
                    )
                }

                // Big "7 DIAS"
                Row(
                    verticalAlignment     = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text       = "7",
                        color      = Aqua,
                        fontSize   = 60.sp,
                        fontWeight = FontWeight.Black,
                    )
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(
                            text       = "DIAS",
                            color      = FitColors.TextPrimary,
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Black,
                        )
                        Text(
                            text     = "PRO grátis",
                            color    = FitColors.TextMuted,
                            fontSize = 13.sp,
                        )
                    }
                }

                Text(
                    text       = "Experimente todos os recursos PRO sem compromisso. Cancele antes do trial expirar e não pague nada.",
                    color      = FitColors.TextMuted,
                    fontSize   = 13.sp,
                    lineHeight = 19.sp,
                )

                // Perks grid — 2 × 2
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    trialPerks.chunked(2).forEach { row ->
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            row.forEach { perk ->
                                TrialPerkChip(perk = perk, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // CTA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(listOf(Aqua, Color(0xFF00CC88))),
                        )
                        .clickable(onClick = onTrial)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text          = "COMEÇAR TRIAL GRATUITO",
                        color         = Color.Black,
                        fontSize      = 13.sp,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 0.8.sp,
                    )
                }

                Text(
                    text      = "Não é necessário cartão de crédito.",
                    color     = FitColors.TextDisabled,
                    fontSize  = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun TrialPerkChip(perk: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Aqua.copy(alpha = 0.07f))
            .border(1.dp, Aqua.copy(alpha = 0.22f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector        = Icons.Rounded.Check,
            contentDescription = null,
            tint               = Aqua,
            modifier           = Modifier.size(12.dp),
        )
        Text(text = perk, color = FitColors.TextMuted, fontSize = 11.sp)
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// Feature Table Card
// ═════════════════════════════════════════════════════════════════════════════

private data class FeatureItem(val label: String, val free: Boolean, val pro: Boolean = true)

private val featureItems = listOf(
    FeatureItem("Treinos ilimitados",       free = false),
    FeatureItem("Rastreamento de carga",    free = true),
    FeatureItem("Histórico básico (30d)",   free = true),
    FeatureItem("Análise avançada de XP",   free = false),
    FeatureItem("IA Personal Trainer",      free = false),
    FeatureItem("Plano alimentar",          free = false),
    FeatureItem("Badges & conquistas",      free = true),
    FeatureItem("Comunidades",              free = true),
    FeatureItem("Ranking global",           free = false),
)

@Composable
private fun FeatureTableCard() {
    FVCard(modifier = Modifier.fillMaxWidth(), glowColor = Gold) {
        Column(modifier = Modifier.padding(16.dp)){
            Row(
                modifier              = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.weight(1f))
                TableColHeader(text = "GRÁTIS", color = FitColors.TextMuted)
                Spacer(Modifier.width(8.dp))
                TableColHeader(text = "PRO", color = Gold)
                Spacer(Modifier.width(2.dp))
            }
            featureItems.forEachIndexed { idx, item ->
                FeatureRow(item = item, shaded = idx % 2 == 0)
            }
        }
    }
}

@Composable
private fun TableColHeader(text: String, color: Color) {
    Box(modifier = Modifier.width(52.dp), contentAlignment = Alignment.Center) {
        Text(text = text, color = color, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
    }
}

@Composable
private fun FeatureRow(item: FeatureItem, shaded: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (shaded) FitColors.Surface3.copy(alpha = 0.45f) else Color.Transparent)
            .padding(vertical = 11.dp, horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text     = item.label,
            color    = FitColors.TextPrimary,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f),
        )
        Box(modifier = Modifier.width(52.dp), contentAlignment = Alignment.Center) {
            CellIcon(available = item.free, isPro = false)
        }
        Spacer(Modifier.width(8.dp))
        Box(modifier = Modifier.width(52.dp), contentAlignment = Alignment.Center) {
            CellIcon(available = item.pro, isPro = true)
        }
    }
}

@Composable
private fun CellIcon(available: Boolean, isPro: Boolean) {
    if (available) {
        Icon(
            imageVector        = Icons.Rounded.Check,
            contentDescription = null,
            tint               = if (isPro) Gold else Lime,
            modifier           = Modifier.size(18.dp),
        )
    } else {
        Icon(
            imageVector        = Icons.Rounded.Lock,
            contentDescription = null,
            tint               = FitColors.TextDisabled,
            modifier           = Modifier.size(15.dp),
        )
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// Pricing Card
// ═════════════════════════════════════════════════════════════════════════════

private val pricingBenefits = listOf(
    "Treinos e exercícios ilimitados",
    "IA Personal Trainer 24/7",
    "Análise avançada de progresso",
    "Plano alimentar personalizado",
    "Ranking global & badges exclusivos",
)

@Composable
private fun PricingCard(isAnnual: Boolean, onToggle: () -> Unit) {
    val accent by animateColorAsState(
        targetValue   = if (isAnnual) Amber else Lime,
        animationSpec = tween(300),
        label         = "pricing_accent",
    )

    FVCard(modifier = Modifier.fillMaxWidth(), glowColor = accent) {
        Column(modifier = Modifier.padding(16.dp),verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Text(
                text          = "FAZER UPGRADE",
                color         = FitColors.TextMuted,
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Black,
                letterSpacing = 1.5.sp,
            )

            // Billing period toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(FitColors.Surface3)
                    .border(1.dp, FitColors.Border, RoundedCornerShape(10.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                BillingTab(
                    label    = "MENSAL",
                    selected = !isAnnual,
                    accent   = Lime,
                    modifier = Modifier.weight(1f),
                    onClick  = { if (isAnnual) onToggle() },
                )
                BillingTab(
                    label    = "ANUAL",
                    selected = isAnnual,
                    accent   = Amber,
                    modifier = Modifier.weight(1f),
                    onClick  = { if (!isAnnual) onToggle() },
                    badge    = if (!isAnnual) "−25%" else null,
                )
            }

            // Price display
            Row(
                verticalAlignment     = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text       = "R$",
                    color      = FitColors.TextMuted,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.padding(bottom = 6.dp),
                )
                Text(
                    text       = if (isAnnual) "14" else "19",
                    color      = FitColors.TextPrimary,
                    fontSize   = 48.sp,
                    fontWeight = FontWeight.Black,
                )
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(text = ",90", color = FitColors.TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    Text(text = "/mês", color = FitColors.TextMuted, fontSize = 11.sp)
                }
                if (isAnnual) {
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Amber.copy(alpha = 0.14f))
                            .border(1.dp, Amber.copy(alpha = 0.40f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text          = "ECONOMIZE 25%",
                            color         = Amber,
                            fontSize      = 9.sp,
                            fontWeight    = FontWeight.Black,
                            letterSpacing = 0.8.sp,
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                pricingBenefits.forEach { BenefitRow(text = it, accent = accent) }
            }

            GradientCta(
                text   = if (isAnnual) "ASSINAR ANUALMENTE" else "ASSINAR MENSALMENTE",
                colors = if (isAnnual) listOf(Amber, Color(0xFFCC9900)) else listOf(Lime, Color(0xFF96CC00)),
            )

            Text(
                text      = "Cancele a qualquer momento. Sem cobranças ocultas.",
                color     = FitColors.TextDisabled,
                fontSize  = 11.sp,
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun BillingTab(
    label    : String,
    selected : Boolean,
    accent   : Color,
    modifier : Modifier,
    onClick  : () -> Unit,
    badge    : String? = null,
) {
    val bg     by animateColorAsState(if (selected) accent.copy(alpha = 0.14f) else Color.Transparent, tween(250), "tab_bg")
    val border by animateColorAsState(if (selected) accent.copy(alpha = 0.45f) else Color.Transparent, tween(250), "tab_border")
    val text   by animateColorAsState(if (selected) accent else FitColors.TextDisabled, tween(250), "tab_text")

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(7.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 9.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(text = label, color = text, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
            if (badge != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Amber.copy(alpha = 0.20f))
                        .padding(horizontal = 5.dp, vertical = 1.dp),
                ) {
                    Text(text = badge, color = Amber, fontSize = 8.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun BenefitRow(text: String, accent: Color) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.12f))
                .border(1.dp, accent.copy(alpha = 0.30f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Rounded.Check, contentDescription = null, tint = accent, modifier = Modifier.size(12.dp))
        }
        Text(text = text, color = FitColors.TextPrimary, fontSize = 13.sp)
    }
}
