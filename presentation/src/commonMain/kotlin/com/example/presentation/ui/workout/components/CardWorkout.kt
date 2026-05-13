package com.example.presentation.ui.workout.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.presentation.theme.FitverseColors

// ─────────────────────────────────────────────────────────────────────────────
// WorkoutMainCard — v2 (senior redesign)
// Improvements:
//   • Radial accent bloom on card background
//   • Category badge (PUSH DAY) + better type hierarchy
//   • Pulsing live dot on timer badge
//   • Muscle group chips (Peito · Ombro · Tríceps)
//   • Gradient progress bar with tip dot
//   • Stats wrapped in a contained surface with icon containers
//   • Gradient CTA button with ExtraBold tracking
// ─────────────────────────────────────────────────────────────────────────────

private val CardBg        = Color(0xFF10101A)
private val CardBg2       = Color(0xFF141420)
private val CardBorder    = Color(0xFF252530)
private val TimerBg       = Color(0xFF1E1E2A)
private val TimerBorder   = Color(0xFF2E2E3A)
private val DividerColor  = Color(0xFF252530)

@Composable
fun WorkoutMainCardy(onStart: () -> Unit) {

    // Pulsing live-dot animation
    val pulse = rememberInfiniteTransition(label = "livePulse")
    val pulseAlpha by pulse.animateFloat(
        initialValue = 1f,
        targetValue  = 0.15f,
        animationSpec = infiniteRepeatable(
            animation  = tween(850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseAlpha",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
            // Subtle radial accent glow emanating from top-left
            .drawBehind {
                drawCircle(
                    brush  = Brush.radialGradient(
                        colors = listOf(
                            FitverseColors.Accent.copy(alpha = 0.08f),
                            Color.Transparent,
                        ),
                        center = Offset.Zero,
                        radius = size.width * 0.90f,
                    ),
                    radius = size.width * 0.90f,
                    center = Offset.Zero,
                )
            }
            .background(CardBg),
    ) {
        Column {

            // ── Top accent strip ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                FitverseColors.Accent.copy(alpha = 0.50f),
                                FitverseColors.Accent,
                                FitverseColors.Accent.copy(alpha = 0.50f),
                                Color.Transparent,
                            )
                        )
                    ),
            )

            Column(modifier = Modifier.padding(20.dp)) {

                // ── Header row ───────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    // Left: badge + title + subtitle
                    Column(modifier = Modifier.weight(1f)) {
                        // Category badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(FitverseColors.Accent.copy(alpha = 0.13f))
                                .border(
                                    0.5.dp,
                                    FitverseColors.Accent.copy(alpha = 0.28f),
                                    RoundedCornerShape(4.dp),
                                )
                                .padding(horizontal = 7.dp, vertical = 3.dp),
                        ) {
                            Text(
                                text          = "PUSH DAY",
                                color         = FitverseColors.Accent,
                                fontSize      = 9.sp,
                                fontWeight    = FontWeight.ExtraBold,
                                letterSpacing = 1.2.sp,
                            )
                        }

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text          = "Hypertrophy A",
                            color         = FitverseColors.TextPrimary,
                            fontSize      = 22.sp,
                            fontWeight    = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                            lineHeight    = 24.sp,
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text          = "Fase 2 · Semana 3",
                            color         = FitverseColors.TextMuted,
                            fontSize      = 12.sp,
                            letterSpacing = 0.1.sp,
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Right: timer badge with live dot
                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(TimerBg)
                                .border(1.dp, TimerBorder, RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(FitverseColors.Red.copy(alpha = pulseAlpha)),
                                )
                                Spacer(Modifier.width(7.dp))
                                Text(
                                    text          = "42:15",
                                    color         = FitverseColors.TextPrimary,
                                    fontSize      = 18.sp,
                                    fontWeight    = FontWeight.Black,
                                    letterSpacing = 0.5.sp,
                                )
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        Text(
                            text          = "duração estimada",
                            color         = FitverseColors.TextMuted,
                            fontSize      = 9.sp,
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Muscle group chips ───────────────────────────────────
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Peito", "Ombro", "Tríceps").forEach { MuscleChip(it) }
                }

                Spacer(Modifier.height(20.dp))

                // ── Plan progress ────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text     = "Progresso do plano",
                        color    = FitverseColors.TextMuted,
                        fontSize = 11.sp,
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text       = "37",
                            color      = FitverseColors.TextPrimary,
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text     = "%",
                            color    = FitverseColors.TextMuted,
                            fontSize = 10.sp,
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))

                // Gradient progress track + tip dot
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(TimerBg),
                ) {
                    // Filled portion
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.37f)
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        FitverseColors.Accent.copy(alpha = 0.70f),
                                        FitverseColors.Accent,
                                    )
                                )
                            ),
                    )
                }
                // Tip dot (placed separately to overflow the track height)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.37f)
                        .wrapContentHeight()
                        .offset(y = (-6).dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .border(2.dp, CardBg, CircleShape)
                            .background(FitverseColors.TextPrimary),
                    )
                }

                Spacer(Modifier.height(14.dp))

                // ── Stats ────────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CardBg2)
                        .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    WorkoutStat(Icons.Rounded.FitnessCenter, "18",   "Séries", FitverseColors.Accent)
                    Box(Modifier.width(1.dp).height(32.dp).background(DividerColor))
                    WorkoutStat(Icons.Rounded.Bolt,          "8.4k", "Volume", FitverseColors.Blue)
                    Box(Modifier.width(1.dp).height(32.dp).background(DividerColor))
                    WorkoutStat(Icons.Rounded.Favorite,      "134",  "BPM",    FitverseColors.Red)
                }

                Spacer(Modifier.height(20.dp))

                // ── CTA button ───────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF6D28D9),  // e.g. Color(0xFF6D28D9)
                                    FitverseColors.Accent,
                                )
                            )
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = ripple(color = FitverseColors.Bg.copy(alpha = 0.15f)),
                            onClick           = onStart,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment   = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector     = Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            tint            = FitverseColors.Bg,
                            modifier        = Modifier.size(22.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text          = "INICIAR TREINO",
                            color         = FitverseColors.Bg,
                            fontSize      = 15.sp,
                            fontWeight    = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp,
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sub-components
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun MuscleChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(FitverseColors.Accent.copy(alpha = 0.10f))
            .border(
                0.5.dp,
                FitverseColors.Accent.copy(alpha = 0.25f),
                RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 9.dp, vertical = 4.dp),
    ) {
        Text(
            text          = label,
            color         = FitverseColors.Accent.copy(alpha = 0.85f),
            fontSize      = 10.sp,
            fontWeight    = FontWeight.SemiBold,
            letterSpacing = 0.3.sp,
        )
    }
}

@Composable
private fun WorkoutStat(
    icon  : ImageVector,
    value : String,
    label : String,
    color : Color,
) {
    Column(
        horizontalAlignment   = Alignment.CenterHorizontally,
        verticalArrangement   = Arrangement.Center,
    ) {
        // Icon container with tinted background
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector     = icon,
                contentDescription = null,
                tint            = color,
                modifier        = Modifier.size(18.dp),
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text          = value,
            color         = FitverseColors.TextPrimary,
            fontSize      = 18.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = (-0.3).sp,
        )
        Text(
            text     = label,
            color    = FitverseColors.TextMuted,
            fontSize = 10.sp,
        )
    }
}