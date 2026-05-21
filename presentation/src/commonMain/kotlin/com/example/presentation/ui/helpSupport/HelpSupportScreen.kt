@file:OptIn(ExperimentalMaterial3Api::class)

package org.fitverse.presentation.ui.helpSupport

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.widgets.FitverseScreenTitle
import org.fitverse.presentation.widgets.FitverseTopAppBar
import org.fitverse.presentation.widgets.SectionLabel
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.theme.ShapeCard

// ── Models ────────────────────────────────────────────────────────────────────

data class QuickStep(val number: Int, val text: String)

data class FaqItem(
    val question: String,
    val answer: String,
)

private val quickSteps = listOf(
    QuickStep(1, "Configure seu perfil de atleta"),
    QuickStep(2, "Escolha seu objetivo principal"),
    QuickStep(3, "Ative seu primeiro plano de treino"),
    QuickStep(4, "Complete suas missões diárias"),
)

private val faqItems = listOf(
    FaqItem(
        "Como funciona o sistema de XP?",
        "Você ganha XP completando treinos, missões diárias e desafios. Acumule XP para subir de nível e desbloquear conquistas exclusivas.",
    ),
    FaqItem(
        "Como criar um plano de treino?",
        "Vá em Treinos > Criar Plano, escolha seus objetivos e a IA montará um plano personalizado baseado no seu perfil.",
    ),
    FaqItem(
        "O que são missões diárias?",
        "Missões são desafios curtos que renovam todo dia à meia-noite. Complete-as para ganhar XP bônus e manter seu Streak.",
    ),
    FaqItem(
        "Como conectar meu wearable?",
        "Acesse Dispositivos no menu, escolha seu wearable e ative a conexão. Suportamos Apple Watch, Garmin, Fitbit e Mi Band.",
    ),
    FaqItem(
        "Como funciona o Streak?",
        "O Streak conta quantos dias consecutivos você completa pelo menos uma missão. Não perca dias para não perder a sequência!",
    ),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun SupportScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var expandedFaq by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        modifier = modifier,
        containerColor = FitColors.Bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FitverseTopAppBar(
                title = "Central de Ajuda",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(FitColors.Bg)
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        ) {
            item {
                FitverseScreenTitle(title = "Central de Ajuda")
                Spacer(Modifier.height(18.dp))
                QuickGuideCard(steps = quickSteps)
                SectionLabel("Perguntas Frequentes")
            }

            itemsIndexed(faqItems) { index, item ->
                FaqRow(
                    item       = item,
                    isExpanded = expandedFaq == index,
                    onClick    = { expandedFaq = if (expandedFaq == index) null else index },
                    modifier   = Modifier.padding(bottom = 8.dp),
                )
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

// ── Quick Guide card ──────────────────────────────────────────────────────────

@Composable
private fun QuickGuideCard(steps: List<QuickStep>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, FitColors.Border, ShapeCard)
            .clip(ShapeCard)
            .background(FitColors.Surface)
            .padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("✦", color = FitColors.Accent, fontSize = 12.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                text          = "GUIA RÁPIDO",
                fontSize      = 11.sp,
                fontWeight    = FontWeight.ExtraBold,
                letterSpacing = 0.8.sp,
                color         = FitColors.Accent,
            )
        }
        Spacer(Modifier.height(12.dp))
        steps.forEach { step ->
            QuickStepRow(step = step)
        }
    }
}

@Composable
private fun QuickStepRow(step: QuickStep) {
    Row(
        modifier          = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(FitColors.PurpleDim)
                .border(1.dp, FitColors.Purple, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text       = step.number.toString(),
                fontSize   = 11.sp,
                fontWeight = FontWeight.Bold,
                color      = FitColors.Purple,
            )
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text     = step.text,
            fontSize = 13.sp,
            color    = FitColors.TextPrimary,
        )
    }
}

// ── FAQ row with expand ───────────────────────────────────────────────────────

@Composable
private fun FaqRow(
    item: FaqItem,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width  = 1.dp,
                color  = if (isExpanded) FitColors.Border2 else FitColors.Border,
                shape  = ShapeCard,
            )
            .clip(ShapeCard)
            .background(FitColors.Surface)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            Text(
                text       = item.question,
                fontSize   = 13.sp,
                fontWeight = FontWeight.Medium,
                color      = FitColors.TextPrimary,
                modifier   = Modifier.weight(1f),
            )
            Icon(
                imageVector        = Icons.Default.ChevronRight,
                contentDescription = null,
                tint               = FitColors.TextMuted,
                modifier           = Modifier
                    .size(18.dp)
                    .rotate(if (isExpanded) 90f else 0f),
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter   = fadeIn(tween(200)) + expandVertically(tween(250)),
            exit    = fadeOut(tween(150)) + shrinkVertically(tween(200)),
        ) {
            Column {
                HorizontalDivider(color = FitColors.Border, thickness = 1.dp)
                Text(
                    text     = item.answer,
                    fontSize = 13.sp,
                    color    = FitColors.TextMuted,
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(14.dp),
                )
            }
        }
    }
}
