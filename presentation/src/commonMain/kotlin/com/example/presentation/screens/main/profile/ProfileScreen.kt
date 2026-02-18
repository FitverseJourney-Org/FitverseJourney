package com.example.presentation.screens.main.profile

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.main.nutrition.TextPrimary
import com.example.presentation.screens.main.nutrition.TextSecondary
import kotlin.jvm.JvmName

// ---------- Theme colors (ajuste conforme seu tema) ----------
private val BaseGreen = Color(0xFF0A160C)
private val DeepGreen = Color(0xFF0F2A17)
private val AccentGreen = Color(0xFF3FAE6A)
private val SurfaceGreen = Color(0xFF132D1C)
private val OnSurfaceText = Color(0xFFFFFFFF)
private val MutedText = Color(0xFFBDBDBD)

// ---------- Models ----------
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val bio: String = "",
    val accountType: com.example.presentation.screens.main.profile.AccountType = _root_ide_package_.com.example.presentation.screens.main.profile.AccountType.FREE,
    val level: Int = 1,
    val xp: Int = 0,
    val stamina: Int = 100,
    val hp: Int = 100,
    val avatarUrl: String? = null // ou null para usar iniciais
)

enum class AccountType { FREE, PREMIUM }

// ---------- Public Profile Screen API ----------
@Composable
fun ProfileScreenV2(
    modifier: Modifier = Modifier,
    initialName: String = "John Doe",
    initialEmail: String = "rafael@gmail.com",
    initialBio: String = "",
    initialAge: String = "25",
    initialWeight: String = "78",
    initialHeight: String = "178",
    initialGoal: String = "Gain Muscle",
    isPremium: Boolean = false,
    onSave: () -> Unit = {},
    onLogout: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onChoosePackage: () -> Unit, // <- nova callback
    goToPlans: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var email by rememberSaveable { mutableStateOf(initialEmail) }
    var bio by rememberSaveable { mutableStateOf(initialBio) }
    var age by rememberSaveable { mutableStateOf(initialAge) }
    var weight by rememberSaveable { mutableStateOf(initialWeight) }
    var height by rememberSaveable { mutableStateOf(initialHeight) }
    var goal by rememberSaveable { mutableStateOf(initialGoal) }
    var premium by rememberSaveable { mutableStateOf(isPremium) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(
                BaseGreen,
                DeepGreen
            ))),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // HEADER
        item {
            ProfileHeaderCard(
                name = name,
                email = email,
                level = 1,
                xp = 0,
                xpForNextLevel = 100,
                hpPercent = 100,
                staminaPercent = 100,
                isPremium = premium,
                onEditProfile = { /* open edit */ },
                onUpgradeAccount = { onChoosePackage() } // -> navegar
            )
        }

        // EDIT PROFILE
        item { _root_ide_package_.com.example.presentation.screens.main.profile.SectionTitle("Edit profile") }
        item {
            _root_ide_package_.com.example.presentation.screens.main.profile.ProfileFormCard {
                _root_ide_package_.com.example.presentation.screens.main.profile.ProfileInput(
                    "Name",
                    name
                ) { name = it }
                _root_ide_package_.com.example.presentation.screens.main.profile.ProfileInput(
                    "Email",
                    email
                ) { email = it }
                _root_ide_package_.com.example.presentation.screens.main.profile.ProfileInput(
                    "Bio",
                    bio,
                    singleLine = false
                ) { bio = it }
            }
        }

        // BODY & GOALS
        item { _root_ide_package_.com.example.presentation.screens.main.profile.SectionTitle("Body & goals") }
        item {
            _root_ide_package_.com.example.presentation.screens.main.profile.ProfileFormCard {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    _root_ide_package_.com.example.presentation.screens.main.profile.ProfileInput(
                        "Age",
                        age,
                        Modifier.weight(1f)
                    ) { age = it }
                    _root_ide_package_.com.example.presentation.screens.main.profile.ProfileInput(
                        "Weight (kg)",
                        weight,
                        Modifier.weight(1f)
                    ) { weight = it }
                    _root_ide_package_.com.example.presentation.screens.main.profile.ProfileInput(
                        "Height (cm)",
                        height,
                        Modifier.weight(1f)
                    ) { height = it }
                }
                _root_ide_package_.com.example.presentation.screens.main.profile.ProfileInput(
                    "Fitness goal",
                    goal
                ) { goal = it }
            }
        }

        // ACCOUNT LEVEL (reutiliza card com CTA para escolha de pacote)
        item { _root_ide_package_.com.example.presentation.screens.main.profile.SectionTitle("Account") }
        item {
            _root_ide_package_.com.example.presentation.screens.main.profile.AccountStatusCard(
                isPremium = premium,
                onUpgrade = { onChoosePackage() },
                onManage = { /* open manage account */ }
            )
        }

        // SAVE
        item {
            Button(
                onClick = {
                    goToPlans()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Save changes", color = Color.Black, fontSize = 16.sp)
            }
        }
    }
}



@Composable
private fun BenefitRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(_root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = text, color = TextPrimary, fontSize = 13.sp)
    }
}

@Composable
@JvmName("AccountOptionComposable")
fun AccountOption(
    label: String,
    subtitle: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = if (selected) _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen.copy(alpha = 0.12f) else Color.Transparent),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Small indicator
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (selected) _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen else Color.White.copy(alpha = 0.04f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label.firstOrNull()?.toString() ?: "",
                    color = if (selected) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
            }

            if (selected) {
                Text(text = "Selected", color = _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}
@Composable
fun AccountStatusCard(
    isPremium: Boolean,
    onUpgrade: () -> Unit,
    onManage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = _root_ide_package_.com.example.presentation.screens.main.profile.SurfaceGreen),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // --- Top: badge / CTA highlight (pulse se free) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        if (!isPremium) {
                            // Pulse sutil para chamar atenção do upgrade
                            val pulse = rememberInfiniteTransition()
                            val scale by pulse.animateFloat(
                                initialValue = 1f,
                                targetValue = 1.06f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(900, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .scale(scale)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(_root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen, _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen.copy(alpha = 0.9f))
                                        )
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(text = "Upgrade", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        } else {
                            // Premium badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.04f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(text = "✓ Premium", color = _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.End) {
                            Text(text = if (isPremium) "Active" else "Basic", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                color = Color.White.copy(alpha = 0.02f),
                                shape = RoundedCornerShape(8.dp),
                                tonalElevation = 0.dp
                            ) {
                                Text(
                                    text = if (isPremium) "Member" else "Try Premium",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    color = if (isPremium) _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen else TextSecondary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isPremium) "Premium account" else "Free account",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isPremium)
                            "You have access to all premium features."
                        else
                            "Unlock AI workouts, advanced insights and priority plans.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }

                // small highlight box with quick stat (optional)

            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- Benefits list (conciso) ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                _root_ide_package_.com.example.presentation.screens.main.profile.BenefitRow(text = "AI workout generation")
                _root_ide_package_.com.example.presentation.screens.main.profile.BenefitRow(text = "Detailed analytics & insights")
                _root_ide_package_.com.example.presentation.screens.main.profile.BenefitRow(text = "Priority support & exclusive plans")
            }

            Spacer(modifier = Modifier.height(18.dp))

            // --- CTA principal ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (!isPremium) {
                    Button(
                        onClick = onUpgrade,
                        colors = ButtonDefaults.buttonColors(containerColor = _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                    ) {
                        Text("Go Premium — Start 7-day trial", fontWeight = FontWeight.Bold)
                    }
                } else {
                    OutlinedButton(
                        onClick = onManage,
                        modifier = Modifier
                            .fillMaxWidth(0.75f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen)
                    ) {
                        Text("Manage subscription", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // --- Footer: trial hint for free users ---
            if (!isPremium) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Start a 7-day free trial — cancel anytime before payment.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}



// ---------- Small components ----------
@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = TextSecondary,
        fontSize = 13.sp,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun ProfileFormCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = _root_ide_package_.com.example.presentation.screens.main.profile.SurfaceGreen),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun ProfileInput(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine
    )
}


// ProfileComponents.kt


// ---------- AccountOption (radio-like) ----------
@Composable
fun AccountOption(
    label: String,
    subtitle: String? = null,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Radio visual customizado (círculo preenchido quando selecionado)
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = if (selected) _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen.copy(alpha = 0.15f) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            RadioButton(
                selected = selected,
                onClick = null, // clique tratado no Row
                colors = RadioButtonDefaults.colors(
                    selectedColor = _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen,
                    unselectedColor = Color.White.copy(alpha = 0.35f),
                    disabledSelectedColor = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = TextPrimary, fontSize = 15.sp)
            subtitle?.let {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = it, color = TextSecondary, fontSize = 12.sp)
            }
        }
    }
}

// ---------- ActionRow (linha clicável com ícone e texto) ----------
@Composable
fun ActionRow(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    danger: Boolean = false,
    modifier: Modifier = Modifier
) {
    val textColor = if (danger) Color(0xFFFF6B6B) else TextPrimary
    val iconTint = if (danger) Color(0xFFFF6B6B) else _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = _root_ide_package_.com.example.presentation.screens.main.profile.SurfaceGreen),
        shape = RoundedCornerShape(14.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
                modifier = Modifier.size(40.dp)
            ) {
                Box(modifier= Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = label, tint = iconTint, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, color = textColor, fontSize = 15.sp)
            }

            // chevron sutil
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "open",
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }

    }
}
@Composable
fun ProfileHeaderCard(
    modifier: Modifier = Modifier,
    name: String,
    email: String?,
    level: Int,
    xp: Int,
    xpForNextLevel: Int,
    hpPercent: Int,
    staminaPercent: Int,
    isPremium: Boolean,
    onEditProfile: () -> Unit = {},
    onUpgradeAccount: () -> Unit = {},
    onAvatarClick: () -> Unit = {}
) {
    // animação sutil ao subir de level
    var previousLevel by remember { mutableStateOf(level) }
    val levelScale by animateFloatAsState(
        targetValue = if (level > previousLevel) 1.06f else 1f,
        animationSpec = spring(stiffness = 450f)
    )

    LaunchedEffect(level) { previousLevel = level }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = _root_ide_package_.com.example.presentation.screens.main.profile.SurfaceGreen)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            /* ───────────────
             * HEADER
             * ─────────────── */
            Row(verticalAlignment = Alignment.CenterVertically) {

                // Avatar
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.06f))
                        .clickable { onAvatarClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = _root_ide_package_.com.example.presentation.screens.main.profile.initials(
                            name
                        ),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(Modifier.width(14.dp))

                // Nome + email
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    email?.let {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = it,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Badge + Edit
                Column(horizontalAlignment = Alignment.End) {
                    _root_ide_package_.com.example.presentation.screens.main.profile.AccountBadge(
                        isPremium
                    )

                }
            }

            Spacer(Modifier.height(16.dp))

            /* ───────────────
             * LEVEL + XP
             * ─────────────── */
            Row(verticalAlignment = Alignment.CenterVertically) {

                // Level badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.05f),
                    modifier = Modifier
                        .size(46.dp)
                        .scale(levelScale)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = level.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.align(Alignment.BottomCenter)
                                .padding(bottom = 2.dp)
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$xp XP • ${xpForNextLevel - xp} to next level",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(Modifier.height(6.dp))

                    val progress = (xp.toFloat() / xpForNextLevel.coerceAtLeast(1))
                        .coerceIn(0f, 1f)
                    val animatedProgress by animateFloatAsState(progress, spring())

                    LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen,
                        trackColor = Color.White.copy(alpha = 0.06f)
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            /* ───────────────
             * STATS
             * ─────────────── */
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                _root_ide_package_.com.example.presentation.screens.main.profile.SmallStat(
                    label = "Health",
                    percent = hpPercent,
                    color = Color(0xFFEF9A9A)
                )
                _root_ide_package_.com.example.presentation.screens.main.profile.SmallStat(
                    label = "Stamina",
                    percent = staminaPercent,
                    color = _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen
                )
            }
        }
    }
}

/* ───────────────
 * COMPONENTES AUXILIARES
 * ─────────────── */

@Composable
private fun AccountBadge(isPremium: Boolean) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isPremium)
            _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen.copy(alpha = 0.14f)
        else
            Color.White.copy(alpha = 0.05f)
    ) {
        Text(
            text = if (isPremium) "PREMIUM" else "FREE",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isPremium) _root_ide_package_.com.example.presentation.screens.main.profile.AccentGreen else TextSecondary
        )
    }
}

@Composable
private fun SmallStat(
    label: String,
    percent: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animated = animateFloatAsState(targetValue = (percent.coerceIn(0, 100) / 100f), animationSpec = spring())
    Column(modifier = modifier.widthIn(min = 80.dp)) {
        Text(text = label, color = TextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = animated.value,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = color,
            trackColor = Color.White.copy(alpha = 0.04f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "${percent}%", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

private fun initials(name: String?): String {
    if (name.isNullOrBlank()) return ""
    return name.trim().split("\\s+".toRegex()).mapNotNull { it.firstOrNull()?.toString()?.uppercase() }
        .take(2).joinToString("")
}