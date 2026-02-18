package com.example.presentation.screens.trial

// TrialIntroScreen.kt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.components.animations.FloatingShapes
import com.example.presentation.theme.TrialAccentGreen
import com.example.presentation.theme.TrialBaseGreen
import com.example.presentation.theme.TrialDeepGreen
import com.example.presentation.theme.TrialSurfaceGreen
import com.example.presentation.theme.TrialTextPrimary
import com.example.presentation.theme.TrialTextSecondary
import fitversejorneyapp.presentation.generated.resources.Res
import fitversejorneyapp.presentation.generated.resources.trial_card_description_1
import fitversejorneyapp.presentation.generated.resources.trial_card_description_2
import fitversejorneyapp.presentation.generated.resources.trial_card_description_3
import fitversejorneyapp.presentation.generated.resources.trial_card_description_4
import fitversejorneyapp.presentation.generated.resources.trial_card_title
import fitversejorneyapp.presentation.generated.resources.trial_cta_text
import fitversejorneyapp.presentation.generated.resources.trial_description_1
import fitversejorneyapp.presentation.generated.resources.trial_flag_day_trial_title
import fitversejorneyapp.presentation.generated.resources.trial_footer_text
import fitversejorneyapp.presentation.generated.resources.trial_rocket
import fitversejorneyapp.presentation.generated.resources.trial_title_1
import fitversejorneyapp.presentation.generated.resources.trial_trust_message_1
import fitversejorneyapp.presentation.generated.resources.trial_trust_message_2
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource


@Composable
fun TrialHeader(
    trialDays: Int,
    modifier: Modifier = Modifier
) {
    // micro bounce para emoji
    val infinite = rememberInfiniteTransition()
    val emojiScale by infinite.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // control visible / entrance
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        // pequeno delay para um efeito de "stagger" com o card
        delay(80)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { full -> -full / 6 },
            animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(360))
    ) {
        Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(TrialAccentGreen.copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = stringResource(Res.string.trial_flag_day_trial_title),
                    color = TrialAccentGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // animated emoji / hero circle for visual interest
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(TrialAccentGreen.copy(alpha = 0.18f), Color.Transparent)
                        )
                    )
                    .scale(emojiScale),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.trial_rocket),
                    fontSize = 28.sp,
                    modifier = Modifier,
                    // acessibilidade simples
                )
            }

            Text(
                text = stringResource(Res.string.trial_title_1),
                color = TrialTextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 34.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.trial_description_1),
                color = TrialTextSecondary,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                maxLines = 2
            )



            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // título + subtitle
                Column(modifier = Modifier.weight(1f)) {

                }

                Spacer(modifier = Modifier.width(12.dp))

                // badge + emoji/block
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    // small pill showing trial length

                }
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun TrialIntroScreen(
    heroAnimation: (@Composable () -> Unit)? = null,
    onStartTrial: () -> Unit,
    onSkip: () -> Unit,
    onViewPlans: () -> Unit = {}
) {

    Scaffold{
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(
                TrialBaseGreen,
                TrialDeepGreen
            )))
            .padding(20.dp)
            .padding(it)
        ) {
            /* ---------- BACKGROUND DECOR ---------- */
            FloatingShapes(modifier = Modifier.matchParentSize())
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                onClick = {
                    onSkip()
                }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = Color.White,
                )
            }
            /* ---------- MAIN CONTENT ---------- */
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // HEADER
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
                // MAIN CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = TrialSurfaceGreen),
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

                        // Brand / Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            TrialAccentGreen.copy(alpha = 0.18f),
                                            TrialAccentGreen.copy(alpha = 0.08f)
                                        )
                                    )
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.trial_card_title),
                                color = TrialAccentGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        // Benefits
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

                        // CTA principal
                        val pulse = rememberInfiniteTransition()
                        val scale by pulse.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.05f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(900, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            )
                        )

                        Button(
                            onClick = onStartTrial,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .scale(scale),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TrialAccentGreen,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = stringResource(Res.string.trial_cta_text),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        // Trust message
                        Text(
                            text = stringResource(Res.string.trial_trust_message_1),
                            color = TrialTextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = stringResource(Res.string.trial_trust_message_2),
                            color = TrialTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }


                // FOOTNOTE
                Text(
                    text = stringResource(Res.string.trial_footer_text),
                    color = TrialTextSecondary,
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
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 120L) // efeito cascata
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(300)
        ) + slideInHorizontally(
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
                    .background(Color.White.copy(alpha = 0.04f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = TrialAccentGreen,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            Text(
                text = text,
                color = TrialTextPrimary,
                fontSize = 14.sp
            )
        }
    }
}
