package org.fitverse.presentation.ui.workout.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import org.fitverse.presentation.theme.FitColors

private val CardBg      = Color(0xFF0F0F18)
private val CardBg2     = Color(0xFF15151E)
private val CardBorder  = Color(0xFF21212E)
private val Divider     = Color(0xFF21212E)

@Composable
fun WorkoutMainCardy(onStart: () -> Unit) {
    val pulse = rememberInfiniteTransition(label = "livePulse")
    val dotAlpha by pulse.animateFloat(
        initialValue  = 1f,
        targetValue   = 0.15f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            RepeatMode.Reverse,
        ),
        label = "dotAlpha",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
            .background(CardBg),
    ) {
        Column {
            // ── Accent strip ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                FitColors.Accent.copy(alpha = 0.5f),
                                FitColors.Accent,
                                FitColors.Accent.copy(alpha = 0.5f),
                                Color.Transparent,
                            )
                        )
                    ),
            )

            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {

                // ── Header ────────────────────────────────────────────────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Top,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text          = "PUSH DAY",
                            color         = FitColors.Accent,
                            fontSize      = 10.sp,
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 2.sp,
                        )
                        Spacer(Modifier.height(5.dp))
                        Text(
                            text          = "Hypertrophy A",
                            color         = FitColors.TextPrimary,
                            fontSize      = 22.sp,
                            fontWeight    = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                            lineHeight    = 24.sp,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text     = "Fase 2 · Semana 3",
                            color    = FitColors.TextMuted,
                            fontSize = 12.sp,
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    // Timer — flat, sem container
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(FitColors.Accent.copy(alpha = dotAlpha)),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text          = "42:15",
                                color         = FitColors.TextPrimary,
                                fontSize      = 20.sp,
                                fontWeight    = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                            )
                        }
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text     = "duração est.",
                            color    = FitColors.TextDisabled,
                            fontSize = 9.sp,
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // ── Muscle chips ──────────────────────────────────────────
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Peito", "Ombro", "Tríceps").forEach { group ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(FitColors.Accent.copy(alpha = 0.08f))
                                .border(0.5.dp, FitColors.Accent.copy(alpha = 0.18f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 9.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text       = group,
                                color      = FitColors.TextMuted,
                                fontSize   = 10.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                // ── Progress ──────────────────────────────────────────────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "Progresso do plano", color = FitColors.TextMuted, fontSize = 11.sp)
                    Text(text = "37%", color = FitColors.Accent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(7.dp))
                LinearProgressIndicator(
                    progress   = { 0.37f },
                    modifier   = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)),
                    color      = FitColors.Accent,
                    trackColor = CardBg2,
                )

                Spacer(Modifier.height(18.dp))

                // ── Stats ─────────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBg2)
                        .border(1.dp, Divider, RoundedCornerShape(12.dp))
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    StatItem(Icons.Rounded.FitnessCenter, "18",   "séries", FitColors.Accent)
                    Box(Modifier.width(1.dp).height(28.dp).background(Divider))
                    StatItem(Icons.Rounded.Bolt,          "8.4k", "volume", FitColors.Blue)
                    Box(Modifier.width(1.dp).height(28.dp).background(Divider))
                    StatItem(Icons.Rounded.Favorite,      "134",  "bpm",    FitColors.Red)
                }

                Spacer(Modifier.height(18.dp))

                // ── CTA ───────────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(FitColors.Accent)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = ripple(color = FitColors.Bg.copy(alpha = 0.20f)),
                            onClick           = onStart,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector        = Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            tint               = FitColors.Bg,
                            modifier           = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text          = "INICIAR TREINO",
                            color         = FitColors.Bg,
                            fontSize      = 14.sp,
                            fontWeight    = FontWeight.ExtraBold,
                            letterSpacing = 1.8.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(icon: ImageVector, value: String, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = color.copy(alpha = 0.80f),
            modifier           = Modifier.size(15.dp),
        )
        Text(
            text          = value,
            color         = FitColors.TextPrimary,
            fontSize      = 17.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = (-0.3).sp,
        )
        Text(
            text     = label,
            color    = FitColors.TextMuted,
            fontSize = 10.sp,
        )
    }
}
