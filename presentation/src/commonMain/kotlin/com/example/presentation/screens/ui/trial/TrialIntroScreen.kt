package com.example.presentation.screens.ui.trial

// TrialIntroScreen.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.activePlan.PlanId
import com.example.presentation.screens.ui.trial.state.TrialUiState
import com.example.presentation.screens.widgets.FitverseButton
import ui.components.*
import ui.theme.FitverseColors

// ── Models ────────────────────────────────────────────────────────────────────



data class Plan(
    val id: PlanId,
    val name: String,
    val nameColor: Color,
    val priceLabel: String,
    val priceSuffix: String,
    val features: List<String>,
    val isMostPopular: Boolean = false,
)

private val plans = listOf(
    Plan(
        id          = PlanId.TRIAL,
        name        = "Trial",
        nameColor   = FitverseColors.TextMuted,
        priceLabel  = "Grátis",
        priceSuffix = "7 dias",
        features    = listOf("Acesso básico", "3 treinos/semana", "Missões básicas", "Comunidade"),
    ),
    Plan(
        id            = PlanId.PRO,
        name          = "Pro",
        nameColor     = FitverseColors.Accent,
        priceLabel    = "R\$ 29,90",
        priceSuffix   = "/mês",
        features      = listOf("Treinos ilimitados", "IA de planos", "Todos os recursos", "Sem anúncios"),
        isMostPopular = true,
    ),
    Plan(
        id          = PlanId.ELITE,
        name        = "Elite",
        nameColor   = FitverseColors.Blue,
        priceLabel  = "R\$ 49,90",
        priceSuffix = "/mês",
        features    = listOf("Tudo do PRO", "Nutricionista IA", "Coaching 1:1", "NFT badges"),
    ),
)

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun TrialIntroScreen(
    state     : TrialUiState,
    onActivate: () -> Unit,
    onDismiss : () -> Unit,
    onSelect  : (PlanId) -> Unit,
) {
    TrialIntroScreenContent(
        selectedPlan = state.selectedPlan,
        isLoading    = state.isLoading,
        onActivate   = onActivate,
        onDismiss    = onDismiss,
        onSelect     = onSelect,
    )
}

@Composable
fun TrialIntroScreenContent(
    selectedPlan : PlanId,
    isLoading    : Boolean,
    onActivate   : () -> Unit,
    onDismiss    : () -> Unit,
    onSelect     : (PlanId) -> Unit,
) {
    LazyColumn(
        modifier            = Modifier.fillMaxSize().background(FitverseColors.Bg),
        contentPadding      = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    ) {
        item { HeroSection() }

        items(plans, key = { it.id }) { plan ->
            PlanCard(
                plan       = plan,
                isSelected = selectedPlan == plan.id,
                onSelect   = { onSelect(plan.id) },
            )
        }

        item {
            Spacer(Modifier.height(6.dp))
            FitverseButton(
                text      = "Ativar Plano",
                onClick   = onActivate,
                isLoading = isLoading,
                enabled   = !isLoading,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text      = "Continuar sem Plano",
                fontSize  = 12.sp,
                color     = FitverseColors.TextMuted,
                textAlign = TextAlign.Center,
                modifier  = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isLoading, onClick = onDismiss),
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Plan card ─────────────────────────────────────────────────────────────────

@Composable
fun HeroSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        Text("👑", fontSize = 48.sp)
        Spacer(Modifier.height(10.dp))
        Text(
            text = "ESCOLHA SEU PLANO",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = FitverseColors.TextPrimary,
            textAlign = TextAlign.Center,
            letterSpacing = 0.5.sp,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Desbloqueie todo o seu potencial",
            fontSize = 13.sp,
            color = FitverseColors.TextMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
    }
}
@Composable
private fun PlanCard(
    plan: Plan,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width  = if (isSelected) 2.dp else 1.dp,
                    color  = if (isSelected) FitverseColors.Accent else FitverseColors.Border,
                    shape  = ShapeCard,
                )
                .clip(ShapeCard)
                .background(if (isSelected) FitverseColors.Surface2 else FitverseColors.Surface)
                .clickable(onClick = onSelect)
                .padding(16.dp),
        ) {
            // Name + radio
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text(
                    text       = plan.name.uppercase(),
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Black,
                    color      = plan.nameColor,
                    letterSpacing = 0.5.sp,
                )
                PlanRadio(isSelected = isSelected)
            }

            Spacer(Modifier.height(4.dp))

            // Price
            Text(
                text = buildAnnotatedString {
                    append(plan.priceLabel)
                    withStyle(SpanStyle(
                        fontSize  = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color     = FitverseColors.TextMuted,
                    )) { append("  ${plan.priceSuffix}") }
                },
                fontSize   = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = FitverseColors.TextPrimary,
            )

            Spacer(Modifier.height(10.dp))

            // Feature chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement   = Arrangement.spacedBy(6.dp),
            ) {
                plan.features.forEach { feat ->
                    FeatureChip(text = "✓ $feat")
                }
            }
        }

        // "Mais Popular" badge
        if (plan.isMostPopular) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-1).dp)
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .background(FitverseColors.Accent)
                    .padding(horizontal = 14.dp, vertical = 4.dp),
            ) {
                Text(
                    text          = "MAIS POPULAR",
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.ExtraBold,
                    letterSpacing = 0.8.sp,
                    color         = FitverseColors.Bg,
                )
            }
        }
    }
}

@Composable
private fun PlanRadio(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(if (isSelected) FitverseColors.Accent else Color.Transparent)
            .border(2.dp, if (isSelected) FitverseColors.Accent else FitverseColors.TextMuted2, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(FitverseColors.Bg),
            )
        }
    }
}

@Composable
private fun FeatureChip(text: String) {
    Box(
        modifier = Modifier
            .border(1.dp, FitverseColors.Border, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(FitverseColors.Surface2)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(text, fontSize = 11.sp, color = FitverseColors.TextPrimary)
    }
}
