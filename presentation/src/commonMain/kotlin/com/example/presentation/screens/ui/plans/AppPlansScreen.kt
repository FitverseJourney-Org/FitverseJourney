package com.example.presentation.screens.ui.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.widgets.FitVerseButton
import com.example.presentation.screens.widgets.FitverseIconBack
import com.example.presentation.screens.widgets.FitverseTopAppBar

data class Plan(
    val title: String,
    val priceLabel: String,
    val isTrial: Boolean = false,
    val isCurrent: Boolean = false,
    val isPremium: Boolean = false,
    val isFree: Boolean = false,
    val features: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPlansScreen(
    onBack: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    var selectedPlanTitle by remember { mutableStateOf("Fitverse Pro") }
    val hasUsedTrial by remember { mutableStateOf(false) }

    val plans = remember(hasUsedTrial) {
        buildList {
            add(Plan(
                title = "Noob Lifter",
                priceLabel = "Free",
                isCurrent = true,
                isPremium = false,
                isFree = true,
                features = listOf(
                    "Access to basic workout tracking",
                    "Limited meal plan templates",
                    "Join up to 1 community challenge"
                )
            ))

            if (!hasUsedTrial) {
                add(Plan(
                    title = "Fitverse Trial",
                    priceLabel = "7 Days Free",
                    isCurrent = false,
                    isPremium = false,
                    isTrial = true,
                    features = listOf(
                        "Try ALL Pro features for 7 days",
                        "AI workout generation included",
                        "No credit card required"
                    )
                ))
            }

            add(Plan(
                title = "Fitverse Pro",
                priceLabel = "$12/month",
                isCurrent = false,
                isPremium = true,
                features = listOf(
                    "Unlimited AI workout generation",
                    "Custom macro & meal planning",
                    "Detailed analytics and 1RM tracking",
                    "Earn +400 XP points monthly"
                )
            ))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            FitverseTopAppBar(
                title = "FITVERSE PLANS",
                onBack = onBack,
            )
        },
        bottomBar = {
            // Lógica atualizada para usar cores frias (Primary e Secondary)
            val bottomBarBg = when (selectedPlanTitle) {
                "Fitverse Pro" -> colors.primary // Azul/Roxo principal
                "Fitverse Trial" -> colors.secondary // Azul claro/Ciano
                else -> colors.surface
            }

            val btnTopColor = when (selectedPlanTitle) {
                "Fitverse Pro" -> colors.onPrimary
                "Fitverse Trial" -> colors.onSecondary
                else -> colors.primary
            }

            val btnEdgeColor = when (selectedPlanTitle) {
                "Fitverse Pro" -> colors.onPrimary.copy(alpha = 0.8f)
                "Fitverse Trial" -> colors.onSecondary.copy(alpha = 0.8f)
                else -> colors.primary.copy(alpha = 0.6f)
            }

            val btnTextColor = when (selectedPlanTitle) {
                "Fitverse Pro" -> colors.primary
                "Fitverse Trial" -> colors.secondary
                else -> colors.onPrimary
            }

            val btnText = when (selectedPlanTitle) {
                "Fitverse Pro" -> "Become a Pro today!"
                "Fitverse Trial" -> "Get your trial!"
                else -> "Keep Current Plan"
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bottomBarBg)
                    .navigationBarsPadding()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cancel anytime without any frustrations.\nYearly plans and more features will be coming soon.",
                    color = when (selectedPlanTitle) {
                        "Fitverse Pro" -> colors.onPrimary.copy(alpha = 0.7f)
                        "Fitverse Trial" -> colors.onSecondary.copy(alpha = 0.7f)
                        else -> colors.onSurfaceVariant
                    },
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                FitVerseButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    text = btnText,
                    topColor = btnTopColor,
                    edgeColor = btnEdgeColor,
                    textColor = btnTextColor,
                    onClick = { /* Fluxo de assinatura */ }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            plans.forEach { plan ->
                PlanCardClean(
                    plan = plan,
                    isSelected = selectedPlanTitle == plan.title,
                    onClick = { selectedPlanTitle = plan.title }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PlanCardClean(
    plan: Plan,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    // Substituição do tertiary pelo primary para garantir o espectro de cores frias
    val containerColor = when {
        plan.isPremium && isSelected -> colors.primary // Cor fria principal para Premium
        plan.isTrial && isSelected -> colors.secondary // Cor fria secundária para Trial
        else -> colors.surface
    }

    val contentColor = when {
        plan.isPremium && isSelected -> colors.onPrimary
        plan.isTrial && isSelected -> colors.onSecondary
        plan.isFree && isSelected -> colors.onSurface
        else -> colors.onSurfaceVariant.copy(alpha = 0.6f)
    }

    val mutedColor = when {
        plan.isPremium && isSelected -> colors.onPrimary.copy(alpha = 0.8f)
        plan.isTrial && isSelected -> colors.onSecondary.copy(alpha = 0.8f)
        plan.isFree && isSelected -> colors.onSurface.copy(alpha = 0.8f)
        else -> colors.onSurfaceVariant
    }

    val enableBtn = plan.isPremium || plan.isTrial

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(containerColor)
                .clickable(enabled = enableBtn) { onClick() }
                .padding(24.dp)
        ) {
            Column {
                if (plan.isTrial) {
                    Text(
                        text = "LIMITED OFFER",
                        color = contentColor.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(Modifier.height(4.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    if (plan.isPremium) colors.onPrimary.copy(alpha = 0.2f)
                                    else colors.primary.copy(alpha = 0.2f),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (plan.isPremium) "👑" else "🛡️", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = plan.title,
                            color = contentColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    Text(
                        text = plan.priceLabel,
                        color = contentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                plan.features.forEach { feature ->
                    Row(
                        modifier = Modifier.padding(bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check",
                            tint = contentColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = feature,
                            color = mutedColor,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        if (plan.isCurrent) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(colors.secondary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Current",
                    color = colors.onSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}