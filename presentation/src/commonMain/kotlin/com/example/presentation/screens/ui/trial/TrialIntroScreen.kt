package com.example.presentation.screens.ui.trial

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.presentation.theme.FitverseColors
import com.example.presentation.ui.components.ShapeCard
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.trial_activate_button
import fitversejourneyapp.presentation.generated.resources.trial_dismiss
import fitversejourneyapp.presentation.generated.resources.trial_feature_ai_nutritionist
import fitversejourneyapp.presentation.generated.resources.trial_feature_ai_plans
import fitversejourneyapp.presentation.generated.resources.trial_feature_all_features
import fitversejourneyapp.presentation.generated.resources.trial_feature_basic_access
import fitversejourneyapp.presentation.generated.resources.trial_feature_basic_missions
import fitversejourneyapp.presentation.generated.resources.trial_feature_coaching
import fitversejourneyapp.presentation.generated.resources.trial_feature_community
import fitversejourneyapp.presentation.generated.resources.trial_feature_everything_pro
import fitversejourneyapp.presentation.generated.resources.trial_feature_nft_badges
import fitversejourneyapp.presentation.generated.resources.trial_feature_no_ads
import fitversejourneyapp.presentation.generated.resources.trial_feature_unlimited_workouts
import fitversejourneyapp.presentation.generated.resources.trial_feature_workouts_per_week
import fitversejourneyapp.presentation.generated.resources.trial_most_popular
import fitversejourneyapp.presentation.generated.resources.trial_plan_name_elite
import fitversejourneyapp.presentation.generated.resources.trial_plan_name_pro
import fitversejourneyapp.presentation.generated.resources.trial_plan_name_trial
import fitversejourneyapp.presentation.generated.resources.trial_price_days
import fitversejourneyapp.presentation.generated.resources.trial_price_free
import fitversejourneyapp.presentation.generated.resources.trial_price_month
import fitversejourneyapp.presentation.generated.resources.trial_subtitle
import fitversejourneyapp.presentation.generated.resources.trial_title
import org.jetbrains.compose.resources.stringResource

// ── Models ────────────────────────────────────────────────────────────────────

data class Plan(
    val id           : PlanId,
    val name         : String,
    val nameColor    : Color,
    val priceLabel   : String,
    val priceSuffix  : String,
    val features     : List<String>,
    val isMostPopular: Boolean = false,
)

/**
 * [plans] era um val top-level com strings hardcoded.
 * Como [stringResource] só pode ser chamado dentro de um Composable,
 * foi convertido para uma função @Composable que retorna a lista.
 *
 * Preços (R$ 29,90 / R$ 49,90) são mantidos como literais pois
 * dependem de lógica de billing/locale, não de tradução de copy.
 */
@Composable
private fun rememberPlans(): List<Plan> = listOf(
    Plan(
        id          = PlanId.TRIAL,
        name        = stringResource(Res.string.trial_plan_name_trial),
        nameColor   = FitverseColors.TextMuted,
        priceLabel  = stringResource(Res.string.trial_price_free),
        priceSuffix = stringResource(Res.string.trial_price_days),
        features    = listOf(
            stringResource(Res.string.trial_feature_basic_access),
            stringResource(Res.string.trial_feature_workouts_per_week),
            stringResource(Res.string.trial_feature_basic_missions),
            stringResource(Res.string.trial_feature_community),
        ),
    ),
    Plan(
        id           = PlanId.PRO,
        name         = stringResource(Res.string.trial_plan_name_pro),
        nameColor    = FitverseColors.Accent,
        priceLabel   = "R\$ 29,90",
        priceSuffix  = stringResource(Res.string.trial_price_month),
        features     = listOf(
            stringResource(Res.string.trial_feature_unlimited_workouts),
            stringResource(Res.string.trial_feature_ai_plans),
            stringResource(Res.string.trial_feature_all_features),
            stringResource(Res.string.trial_feature_no_ads),
        ),
        isMostPopular = true,
    ),
    Plan(
        id          = PlanId.ELITE,
        name        = stringResource(Res.string.trial_plan_name_elite),
        nameColor   = FitverseColors.Blue,
        priceLabel  = "R\$ 49,90",
        priceSuffix = stringResource(Res.string.trial_price_month),
        features    = listOf(
            stringResource(Res.string.trial_feature_everything_pro),
            stringResource(Res.string.trial_feature_ai_nutritionist),
            stringResource(Res.string.trial_feature_coaching),
            stringResource(Res.string.trial_feature_nft_badges),
        ),
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
    val plans = rememberPlans()

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
                text      = stringResource(Res.string.trial_activate_button),
                onClick   = onActivate,
                isLoading = isLoading,
                enabled   = !isLoading,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text      = stringResource(Res.string.trial_dismiss),
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

// ── Hero ──────────────────────────────────────────────────────────────────────

@Composable
fun HeroSection() {
    Column(
        modifier            = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        Text("👑", fontSize = 48.sp)
        Spacer(Modifier.height(10.dp))
        Text(
            text          = stringResource(Res.string.trial_title),
            fontSize      = 28.sp,
            fontWeight    = FontWeight.Black,
            color         = FitverseColors.TextPrimary,
            textAlign     = TextAlign.Center,
            letterSpacing = 0.5.sp,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text      = stringResource(Res.string.trial_subtitle),
            fontSize  = 13.sp,
            color     = FitverseColors.TextMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
    }
}

// ── Plan card ─────────────────────────────────────────────────────────────────

@Composable
private fun PlanCard(
    plan     : Plan,
    isSelected: Boolean,
    onSelect  : () -> Unit,
    modifier  : Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) FitverseColors.Accent else FitverseColors.Border,
                    shape = ShapeCard,
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
                    text          = plan.name.uppercase(),
                    fontSize      = 18.sp,
                    fontWeight    = FontWeight.Black,
                    color         = plan.nameColor,
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
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color      = FitverseColors.TextMuted,
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
                    text          = stringResource(Res.string.trial_most_popular),
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