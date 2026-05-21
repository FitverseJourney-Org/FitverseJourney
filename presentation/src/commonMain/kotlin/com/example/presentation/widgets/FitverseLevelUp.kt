package org.fitverse.presentation.widgets

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.domain.models.levelUp.LevelUpData
import kotlin.math.PI
import kotlin.random.Random

// ─────────────────────────────────────────────────────────────────────────────
//  Design tokens
// ─────────────────────────────────────────────────────────────────────────────

private val BgDeep      = Color(0xFF080912)
private val AccentLime  = Color(0xFFCCFF00)
private val PurpleGlow  = Color(0xFF6040FF)
private val CardBg      = Color(0xFF13142A)
private val BadgePurple = Color(0xFF7B52FF)
private val RingInner   = Color(0xFF1A1C34)

private val particleColors = listOf(
    Color(0xFFCCFF00), // lime
    Color(0xFF7B52FF), // purple
    Color(0xFFFF6B35), // orange
    Color(0xFF35FFD4), // teal
    Color(0xFFFFE135), // yellow
    Color(0xFFFF3580), // pink
    Color(0xFF35C8FF), // sky
)


// ─────────────────────────────────────────────────────────────────────────────
//  Particle model — plain class (NOT Compose State) for performance
// ─────────────────────────────────────────────────────────────────────────────

private class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var life: Float,       // 0..1, fades as it dies
    val decay: Float,      // life lost per frame
    val baseSize: Float,
    val color: Color,
    val isSquare: Boolean,
    val rotationSpeed: Float  // degrees per frame (squares only)
) {
    var rotation = Random.nextFloat() * 360f
}

/** Spawn a particle at a random position across the screen. */
private fun spawnParticle(screenW: Float, screenH: Float): Particle = Particle(
    x             = Random.nextFloat() * screenW,
    y             = Random.nextFloat() * screenH,
    vx            = (Random.nextFloat() - 0.5f) * 1.4f,
    vy            = (Random.nextFloat() - 0.8f) * 1.6f,   // bias upward
    life          = Random.nextFloat() * 0.7f + 0.3f,      // stagger so screen doesn't start empty
    decay         = Random.nextFloat() * 0.004f + 0.0015f,
    baseSize      = Random.nextFloat() * 6f + 3f,
    color         = particleColors.random(),
    isSquare      = Random.nextBoolean(),
    rotationSpeed = (Random.nextFloat() - 0.5f) * 4f
)

/** Draw one particle on the canvas. */
private fun DrawScope.drawParticle(p: Particle) {
    val alpha = p.life.coerceIn(0f, 1f)
    val col   = p.color.copy(alpha = alpha)
    val half  = p.baseSize / 2f
    if (p.isSquare) {
        rotate(degrees = p.rotation, pivot = Offset(p.x, p.y)) {
            drawRect(col, topLeft = Offset(p.x - half, p.y - half), size = Size(p.baseSize, p.baseSize))
        }
    } else {
        drawCircle(col, radius = half, center = Offset(p.x, p.y))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Ring arc helper — orbiting sparkle lines around the avatar
// ─────────────────────────────────────────────────────────────────────────────

private fun DrawScope.drawOrbitalRing(
    center: Offset,
    radius: Float,
    angleOffset: Float,
    color: Color,
    alpha: Float,
) {
    val segments = 6
    val arcLength = (PI / segments).toFloat()
    repeat(segments) { i ->
        val startAngle = angleOffset + i * (360f / segments)
        drawArc(
            color        = color.copy(alpha = alpha),
            startAngle   = startAngle,
            sweepAngle   = arcLength * (180f / PI).toFloat(),
            useCenter    = false,
            topLeft      = Offset(center.x - radius, center.y - radius),
            size         = Size(radius * 2, radius * 2),
            style        = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  LevelUpScreen — Entry point
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Full-screen overlay shown when the player levels up.
 *
 * @param data        All the level-up metadata to display.
 * @param onContinue  Called when the player taps "Continuar Jornada".
 *
 * Usage:
 *   LevelUpScreen(
 *     data = LevelUpData(userName = "Alex", level = 24, className = "Warrior", xpGained = 200),
 *     onContinue = { navController.popBackStack() }
 *   )
 */
@Composable
fun LevelUpScreen(
    data: LevelUpData = LevelUpData(),
    onContinue: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        val screenW = constraints.maxWidth.toFloat()
        val screenH = constraints.maxHeight.toFloat()

        // ── Particle system ───────────────────────────────────────────────────
        // Stored as a plain MutableList (not State) — mutations are driven by
        // withFrameNanos and Compose re-reads via the frameTick trigger below.
        val particles = remember {
            MutableList(70) { spawnParticle(screenW, screenH) }
        }
        var frameTick by remember { mutableStateOf(0L) }

        LaunchedEffect(Unit) {
            while (true) {
                withFrameNanos { nanos ->
                    for (i in particles.indices) {
                        val p = particles[i]
                        if (p.life <= 0f) {
                            // Recycle dead particle instead of allocating a new one
                            particles[i] = spawnParticle(screenW, screenH).apply { life = 1f }
                        } else {
                            p.x        += p.vx
                            p.y        += p.vy
                            p.life     -= p.decay
                            p.rotation += p.rotationSpeed
                            // Wrap around screen edges
                            if (p.x < -20f) p.x = screenW + 20f
                            if (p.x > screenW + 20f) p.x = -20f
                            if (p.y < -20f) p.y = screenH + 20f
                        }
                    }
                    frameTick = nanos
                }
            }
        }

        // ── Infinite transition — glow + ring rotation ─────────────────────
        val inf = rememberInfiniteTransition(label = "inf")

        val glowRadius by inf.animateFloat(
            initialValue  = 0.38f, targetValue = 0.62f,
            animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label         = "glowR"
        )
        val ringAngle by inf.animateFloat(
            initialValue  = 0f, targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Restart),
            label         = "ringAngle"
        )
        val ringAlpha by inf.animateFloat(
            initialValue  = 0.35f, targetValue = 0.80f,
            animationSpec = infiniteRepeatable(tween(1400), RepeatMode.Reverse),
            label         = "ringA"
        )
        val levelPulse by inf.animateFloat(
            initialValue  = 1.00f, targetValue = 1.04f,
            animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label         = "pulse"
        )

        // ── Entry animation ───────────────────────────────────────────────────
        var entered by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { entered = true }
        val contentAlpha by animateFloatAsState(
            targetValue   = if (entered) 1f else 0f,
            animationSpec = tween(700, easing = FastOutSlowInEasing),
            label         = "contentAlpha"
        )
        val contentScale by animateFloatAsState(
            targetValue   = if (entered) 1f else 0.80f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness    = Spring.StiffnessMediumLow
            ),
            label = "contentScale"
        )

        // ── Background canvas — particles + radial glow ───────────────────────
        // Reading frameTick here ensures this Canvas recomposes on every frame.
        @Suppress("UNUSED_EXPRESSION")
        val s_ = frameTick

        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height * 0.42f)

            // Radial purple glow behind everything
            drawCircle(
                brush  = Brush.radialGradient(
                    colors = listOf(PurpleGlow.copy(alpha = 0.30f), Color.Transparent),
                    center = center,
                    radius = size.minDimension * glowRadius
                ),
                radius = size.minDimension * glowRadius,
                center = center
            )

            // Orbiting dashed ring (outer)
            drawOrbitalRing(center, size.minDimension * 0.38f, ringAngle, PurpleGlow, ringAlpha * 0.6f)

            // Particles
            for (p in particles) drawParticle(p)
        }

        // ── Main UI content ───────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .graphicsLayer {
                    alpha  = contentAlpha
                    scaleX = contentScale
                    scaleY = contentScale
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ── Header ────────────────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 52.dp)
            ) {
                Text(
                    text          = data.userName,
                    color         = Color.White,
                    fontSize      = 20.sp,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 2.8.sp
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text          = "PARABÉNS!",
                    color         = Color.White.copy(alpha = 0.65f),
                    fontSize      = 14.sp,
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text          = "LEVEL UP!",
                    color         = AccentLime,
                    fontSize      = 32.sp,
                    fontWeight    = FontWeight.ExtraBold,
                    letterSpacing = 4.sp
                )
            }

            // ── Avatar + level number ─────────────────────────────────────────
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AvatarCircle(level = data.level, ringAngle = ringAngle, ringAlpha = ringAlpha)
                Spacer(Modifier.height(22.dp))
                Text(
                    text       = "${data.level}",
                    color      = AccentLime,
                    fontSize   = 90.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 90.sp,
                    modifier   = Modifier.graphicsLayer { scaleX = levelPulse; scaleY = levelPulse }
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text          = "${data.className} • LEVEL ${data.level}",
                    color         = Color.White.copy(alpha = 0.50f),
                    fontSize      = 13.sp,
                    letterSpacing = 1.8.sp
                )
            }

            // ── Rewards + CTA ─────────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 44.dp)
            ) {
                Row(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RewardCard(modifier = Modifier.weight(1f), icon = "🏆", label = data.badgeLabel)
                    RewardCard(modifier = Modifier.weight(1f), icon = "✨", label = "+${data.xpGained} XP")
                    RewardCard(modifier = Modifier.weight(1f), icon = "🎁", label = "Reward")
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick  = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = AccentLime),
                    shape    = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Text(
                        text          = "CONTINUAR JORNADA  →",
                        color         = Color.Black,
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 1.8.sp
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  AvatarCircle
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AvatarCircle(level: Int, ringAngle: Float, ringAlpha: Float) {
    Box(contentAlignment = Alignment.BottomCenter) {

        // Animated glow halo drawn on canvas
        Canvas(modifier = Modifier.size(180.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            // Soft outer glow
            drawCircle(
                brush  = Brush.radialGradient(
                    colors = listOf(PurpleGlow.copy(alpha = ringAlpha * 0.55f), Color.Transparent),
                    center = center,
                    radius = size.minDimension / 2
                ),
                radius = size.minDimension / 2,
                center = center
            )
            // Spinning arc ring
            drawOrbitalRing(center, size.minDimension * 0.47f, ringAngle, AccentLime, ringAlpha * 0.45f)
            drawOrbitalRing(center, size.minDimension * 0.47f, -ringAngle * 0.6f, PurpleGlow, ringAlpha * 0.35f)
        }

        // Dark circle + icon
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF21224A), RingInner)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(124.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF181929)),
                contentAlignment = Alignment.Center
            ) {
                Text("⚔️", fontSize = 58.sp)
            }
        }

        // "LVL xx" pill badge — offset down to overlap circle bottom
        Box(
            modifier = Modifier
                .offset(y = 14.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BadgePurple)
                .padding(horizontal = 18.dp, vertical = 5.dp)
        ) {
            Text(
                text          = "LVL $level",
                color         = Color.White,
                fontSize      = 12.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  RewardCard
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun RewardCard(modifier: Modifier, icon: String, label: String) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(CardBg)
            .padding(vertical = 20.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = icon, fontSize = 30.sp)
        Spacer(Modifier.height(10.dp))
        Text(
            text          = label,
            color         = Color.White.copy(alpha = 0.82f),
            fontSize      = 11.sp,
            fontWeight    = FontWeight.SemiBold,
            textAlign     = TextAlign.Center,
            letterSpacing = 0.3.sp,
            maxLines      = 2
        )
    }
}