package com.example.presentation.screens.ui.trial

// TrialIntroScreen.kt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.components.animations.FloatingShapes
import com.example.presentation.screens.widgets.FitVerseButton
import com.example.presentation.theme.TrialAccentGreen
import com.example.presentation.theme.TrialTextPrimary
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.trial_card_description_1
import fitversejourneyapp.presentation.generated.resources.trial_card_description_2
import fitversejourneyapp.presentation.generated.resources.trial_card_description_3
import fitversejourneyapp.presentation.generated.resources.trial_card_description_4
import fitversejourneyapp.presentation.generated.resources.trial_card_title
import fitversejourneyapp.presentation.generated.resources.trial_cta_text
import fitversejourneyapp.presentation.generated.resources.trial_description_1
import fitversejourneyapp.presentation.generated.resources.trial_flag_day_trial_title
import fitversejourneyapp.presentation.generated.resources.trial_footer_text
import fitversejourneyapp.presentation.generated.resources.trial_rocket
import fitversejourneyapp.presentation.generated.resources.trial_title_1
import fitversejourneyapp.presentation.generated.resources.trial_trust_message_1
import fitversejourneyapp.presentation.generated.resources.trial_trust_message_2
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource


@Composable
fun TrialHeader(
    trialDays: Int,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val infinite = rememberInfiniteTransition()
    val emojiScale by infinite.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(colors.primary.copy(alpha = 0.12f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = stringResource(Res.string.trial_flag_day_trial_title),
                color = colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.radialGradient(
                        listOf(
                            colors.primary.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    )
                )
                .scale(emojiScale),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.trial_rocket),
                fontSize = 28.sp
            )
        }

        Text(
            text = stringResource(Res.string.trial_title_1),
            color = colors.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 34.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.trial_description_1),
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            maxLines = 2
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrialIntroScreen(
    heroAnimation: (@Composable () -> Unit)? = null,
    onStartTrial: () -> Unit,
    onSkip: () -> Unit,
    onViewPlans: () -> Unit = {}
) {

    val colors = MaterialTheme.colorScheme

    // Gradiente principal usando apenas o tema
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            colors.background,
            colors.surface,
            colors.surfaceVariant
        )
    )

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding)
                .padding(20.dp)
        ) {

            // Floating shapes usa colorScheme internamente
            FloatingShapes(modifier = Modifier.matchParentSize())

            // Scrim leve para reforçar contraste
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.06f))
            )

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onSkip
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = colors.onBackground
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TrialHeader(
                    trialDays = 7,
                    modifier = Modifier.fillMaxWidth()
                )

                val benefits = listOf(
                    stringResource(Res.string.trial_card_description_1),
                    stringResource(Res.string.trial_card_description_2),
                    stringResource(Res.string.trial_card_description_3),
                    stringResource(Res.string.trial_card_description_4)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colors.surfaceVariant
                    ),
                    shape = RoundedCornerShape(22.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {

                        // Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            colors.primary.copy(alpha = 0.18f),
                                            colors.secondary.copy(alpha = 0.08f)
                                        )
                                    )
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.trial_card_title),
                                color = colors.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            benefits.forEachIndexed { index, text ->
                                StaggeredBenefit(
                                    text = text,
                                    index = index,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        val pulse = rememberInfiniteTransition()
                        val scale by pulse.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.05f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(900, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            )
                        )

                        FitVerseButton(
                            text = stringResource(Res.string.trial_cta_text),
                            onClick = onStartTrial,
                            modifier = Modifier
                                .fillMaxWidth()
                                .scale(scale),
                        )

                        Text(
                            text = stringResource(Res.string.trial_trust_message_1),
                            color = colors.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                        Text(
                            text = stringResource(Res.string.trial_trust_message_2),
                            color = colors.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                }

                Text(
                    text = stringResource(Res.string.trial_footer_text),
                    color = colors.onSurfaceVariant,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
private fun DefaultHero() {
    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.radialGradient(
                    listOf(TrialAccentGreen.copy(alpha = 0.18f), Color.Transparent)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Fitverse",
            color = TrialTextPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}





@Composable
fun StaggeredBenefit(
    text: String,
    index: Int,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 120L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) +
                slideInHorizontally(
                    initialOffsetX = { it / 4 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ),
        exit = fadeOut()
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceVariant.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            Text(
                text = text,
                color = colors.onSurface,
                fontSize = 14.sp
            )
        }
    }
}
