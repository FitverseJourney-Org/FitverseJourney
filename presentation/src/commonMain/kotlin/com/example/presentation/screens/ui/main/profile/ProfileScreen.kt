package com.example.presentation.screens.ui.main.profile

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
import kotlin.jvm.JvmName

// ---------- Models ----------
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val bio: String = "",
    val accountType: AccountType = AccountType.FREE,
    val level: Int = 1,
    val xp: Int = 0,
    val stamina: Int = 100,
    val hp: Int = 100,
    val avatarUrl: String? = null
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
    navigateToPlans: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

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
            .background(
                Brush.verticalGradient(
                    listOf(
                        cs.background,
                        cs.surface
                    )
                )
            ),
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
                onUpgradeAccount = { onChoosePackage() }
            )
        }

        // EDIT PROFILE
        item { SectionTitle("Edit profile", cs) }
        item {
            ProfileFormCard(cs) {
                ProfileInput(
                    "Name",
                    name
                ) { name = it }
                ProfileInput(
                    "Email",
                    email
                ) { email = it }
                ProfileInput(
                    "Bio",
                    bio,
                    singleLine = false
                ) { bio = it }
            }
        }

        // BODY & GOALS
        item { SectionTitle("Body & goals", cs) }
        item {
            ProfileFormCard(cs) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileInput(
                        "Age",
                        age,
                        Modifier.weight(1f)
                    ) { age = it }
                    ProfileInput(
                        "Weight (kg)",
                        weight,
                        Modifier.weight(1f)
                    ) { weight = it }
                    ProfileInput(
                        "Height (cm)",
                        height,
                        Modifier.weight(1f)
                    ) { height = it }
                }
                ProfileInput(
                    "Fitness goal",
                    goal
                ) { goal = it }
            }
        }

        // ACCOUNT LEVEL (reutiliza card com CTA para escolha de pacote)
        item { SectionTitle("Account", cs) }
        item {
            AccountStatusCard(
                cs = cs,
                isPremium = premium,
                onUpgrade = { onChoosePackage() },
                onManage = { /* open manage account */ }
            )
        }

        // SAVE
        item {
            Button(
                onClick = { navigateToPlans() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cs.primary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Save changes", color = cs.onPrimary, fontSize = 16.sp)
            }
        }
    }
}

// ---------- Small components ----------
@Composable
private fun SectionTitle(text: String, cs: ColorScheme) {
    Text(
        text = text,
        color = cs.onSurface.copy(alpha = 0.75f),
        fontSize = 13.sp,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun ProfileFormCard(cs: ColorScheme, content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
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
    val cs = MaterialTheme.colorScheme
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label, color = cs.onSurface.copy(alpha = 0.8f)) },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = cs.onSurface,
            focusedTextColor = cs.onSurface,
            focusedContainerColor = cs.surface,
            unfocusedContainerColor = cs.surface,
            focusedBorderColor = cs.primary,
            unfocusedBorderColor = cs.onSurface.copy(alpha = 0.08f),
        )
    )
}

@Composable
private fun BenefitRow(text: String) {
    val cs = MaterialTheme.colorScheme
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(cs.primary)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = text, color = cs.onSurface, fontSize = 13.sp)
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
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) cs.primary.copy(alpha = 0.12f) else Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (selected) cs.primary else cs.onSurface.copy(alpha = 0.04f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label.firstOrNull()?.toString() ?: "",
                    color = if (selected) cs.onPrimary else cs.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, color = cs.onSurface, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, color = cs.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
            }

            if (selected) {
                Text(text = "Selected", color = cs.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun AccountStatusCard(
    cs: ColorScheme,
    isPremium: Boolean,
    onUpgrade: () -> Unit,
    onManage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (!isPremium) {
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
                                            listOf(cs.primary, cs.primary.copy(alpha = 0.9f))
                                        )
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(text = "Upgrade", color = cs.onPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(cs.onSurface.copy(alpha = 0.04f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(text = "✓ Premium", color = cs.primary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                            Text(text = if (isPremium) "Active" else "Basic", color = cs.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = cs.onSurface.copy(alpha = 0.02f),
                                shape = RoundedCornerShape(8.dp),
                                tonalElevation = 0.dp
                            ) {
                                Text(
                                    text = if (isPremium) "Member" else "Try Premium",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    color = if (isPremium) cs.primary else cs.onSurface.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isPremium) "Premium account" else "Free account",
                        color = cs.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isPremium)
                            "You have access to all premium features."
                        else
                            "Unlock AI workouts, advanced insights and priority plans.",
                        color = cs.onSurface.copy(alpha = 0.75f),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BenefitRow(text = "AI workout generation")
                BenefitRow(text = "Detailed analytics & insights")
                BenefitRow(text = "Priority support & exclusive plans")
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (!isPremium) {
                    Button(
                        onClick = onUpgrade,
                        colors = ButtonDefaults.buttonColors(containerColor = cs.primary, contentColor = cs.onPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(0.75f)
                    ) {
                        Text("Go Premium — Start 7-day trial", fontWeight = FontWeight.Bold)
                    }
                } else {
                    OutlinedButton(
                        onClick = onManage,
                        modifier = Modifier.fillMaxWidth(0.75f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = cs.primary)
                    ) {
                        Text("Manage subscription", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            if (!isPremium) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Start a 7-day free trial — cancel anytime before payment.",
                    color = cs.onSurface.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ---------- Small helpers ----------
@Composable
fun AccountOption(
    label: String,
    subtitle: String? = null,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = if (selected) cs.primary.copy(alpha = 0.15f) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = cs.primary,
                    unselectedColor = cs.onSurface.copy(alpha = 0.35f),
                    disabledSelectedColor = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = cs.onSurface, fontSize = 15.sp)
            subtitle?.let {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = it, color = cs.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ActionRow(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    danger: Boolean = false,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val textColor = if (danger) cs.error else cs.onSurface
    val iconTint = if (danger) cs.error else cs.primary

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        shape = RoundedCornerShape(14.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = cs.onSurface.copy(alpha = 0.03f)),
                modifier = Modifier.size(40.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = label, tint = iconTint, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, color = textColor, fontSize = 15.sp)
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "open",
                tint = cs.onSurface.copy(alpha = 0.5f),
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
    val cs = MaterialTheme.colorScheme

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
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(cs.onSurface.copy(alpha = 0.06f))
                        .clickable { onAvatarClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials(name),
                        color = cs.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = cs.onSurface
                    )
                    email?.let {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = it,
                            fontSize = 13.sp,
                            color = cs.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    AccountBadge(isPremium, cs)
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = cs.onSurface.copy(alpha = 0.05f),
                    modifier = Modifier
                        .size(46.dp)
                        .scale(levelScale)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = cs.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = level.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = cs.onSurface,
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
                        color = cs.onSurface.copy(alpha = 0.75f)
                    )

                    Spacer(Modifier.height(6.dp))

                    val progress = (xp.toFloat() / xpForNextLevel.coerceAtLeast(1)).coerceIn(0f, 1f)
                    val animatedProgress by animateFloatAsState(progress, spring())

                    LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = cs.primary,
                        trackColor = cs.onSurface.copy(alpha = 0.06f)
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SmallStat(
                    label = "Health",
                    percent = hpPercent,
                    color = cs.error
                )
                SmallStat(
                    label = "Stamina",
                    percent = staminaPercent,
                    color = cs.secondary
                )
            }
        }
    }
}

@Composable
private fun AccountBadge(isPremium: Boolean, cs: ColorScheme) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isPremium) cs.primary.copy(alpha = 0.14f) else cs.onSurface.copy(alpha = 0.04f)
    ) {
        Text(
            text = if (isPremium) "PREMIUM" else "FREE",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isPremium) cs.onPrimary else cs.onSurface.copy(alpha = 0.8f)
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
    val cs = MaterialTheme.colorScheme
    val animated = animateFloatAsState(targetValue = (percent.coerceIn(0, 100) / 100f), animationSpec = spring())
    Column(modifier = modifier.widthIn(min = 80.dp)) {
        Text(text = label, color = cs.onSurface.copy(alpha = 0.75f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = animated.value,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = color,
            trackColor = cs.onSurface.copy(alpha = 0.04f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "${percent}%", color = cs.onSurface, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

private fun initials(name: String?): String {
    if (name.isNullOrBlank()) return ""
    return name.trim().split(' ').mapNotNull { it.firstOrNull()?.toString()?.uppercase() }
        .take(2).joinToString("")
}