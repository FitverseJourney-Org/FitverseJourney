package com.example.presentation.screens.ui.main.profile

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
fun ProfileScreenPro(
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
    onChoosePackage: () -> Unit = {},
    navigateToPlans: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme

    var name by rememberSaveable { mutableStateOf(initialName) }
    var email by rememberSaveable { mutableStateOf(initialEmail) }
    var bio by rememberSaveable { mutableStateOf(initialBio) }
    var age by rememberSaveable { mutableStateOf(initialAge) }
    var weight by rememberSaveable { mutableStateOf(initialWeight) }
    var height by rememberSaveable { mutableStateOf(initialHeight) }
    var goal by rememberSaveable { mutableStateOf(initialGoal) }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(cs.surface, cs.background))),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // Espaçamento mais generoso
    ) {
        // HEADER GAMIFICADO
        item {
            ProfileHeaderCardPro(
                name = name,
                email = email,
                level = 12,
                xp = 450,
                xpForNextLevel = 1000,
                hpPercent = 80,
                staminaPercent = 60,
                isPremium = isPremium,
                cs = cs
            )
        }

        // EDIT PROFILE
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SectionTitlePro("Personal Info", Icons.Rounded.Person, cs)
                ProfileFormContainer(cs) {
                    ProfileInputPro("Full Name", name, cs) { name = it }
                    ProfileInputPro("Email Address", email, cs) { email = it }
                    ProfileInputPro("Bio", bio, cs, singleLine = false) { bio = it }
                }
            }
        }

        // BODY & GOALS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SectionTitlePro("Body & Goals", Icons.Rounded.FitnessCenter, cs)
                ProfileFormContainer(cs) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProfileInputPro("Age", age, cs, Modifier.weight(1f)) { age = it }
                        ProfileInputPro("Weight (kg)", weight, cs, Modifier.weight(1f)) { weight = it }
                        ProfileInputPro("Height (cm)", height, cs, Modifier.weight(1f)) { height = it }
                    }
                    ProfileInputPro("Main Goal", goal, cs) { goal = it }
                }
            }
        }

        // ACCOUNT STATUS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SectionTitlePro("Subscription", Icons.Rounded.WorkspacePremium, cs)
                AccountStatusCardPro(
                    isPremium = isPremium,
                    onUpgrade = onChoosePackage,
                    onManage = { /* manage */ },
                    cs = cs
                )
            }
        }

        // SAVE BUTTON
        item {
            Button(
                onClick = {
                    onSave()
                    navigateToPlans()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Botão mais alto e imponente
                colors = ButtonDefaults.buttonColors(containerColor = cs.primary),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Save Changes", color = cs.onPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

/* -------------------------------------------------------------------------- */
/* HEADER GAMIFICADO (Avatar, Nível e Barras)                                 */
/* -------------------------------------------------------------------------- */
@Composable
fun ProfileHeaderCardPro(
    name: String, email: String, level: Int, xp: Int, xpForNextLevel: Int,
    hpPercent: Int, staminaPercent: Int, isPremium: Boolean, cs: ColorScheme
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f)),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Linha 1: Avatar e Info Básica
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(cs.primary, cs.tertiary))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.split(" ").take(2).joinToString("") { it.take(1) }.uppercase(),
                        color = cs.onPrimary, fontWeight = FontWeight.Black, fontSize = 22.sp
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(name, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = cs.onSurface)
                    Text(email, fontSize = 13.sp, color = cs.onSurfaceVariant)
                }

                // Badge Premium/Free
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isPremium) cs.primary.copy(alpha = 0.15f) else cs.outline.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = if (isPremium) "PRO" else "FREE",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontSize = 11.sp, fontWeight = FontWeight.Black,
                        color = if (isPremium) cs.primary else cs.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Linha 2: XP e Nível
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cs.secondary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Rounded.Star, null, tint = cs.secondary, modifier = Modifier.size(16.dp))
                        Text("Lvl $level", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = cs.secondary)
                    }
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Experience", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = cs.onSurface)
                        Text("$xp / $xpForNextLevel", fontSize = 12.sp, color = cs.onSurfaceVariant)
                    }
                    Spacer(Modifier.height(8.dp))
                    val progress by animateFloatAsState(targetValue = xp.toFloat() / xpForNextLevel, tween(1000))
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = cs.secondary, trackColor = cs.outline.copy(alpha = 0.1f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = cs.outline.copy(alpha = 0.1f))
            Spacer(Modifier.height(20.dp))

            // Linha 3: HP e Stamina
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ArcadeBarPro("Health", hpPercent, cs.error, Modifier.weight(1f), cs)
                ArcadeBarPro("Stamina", staminaPercent, cs.primary, Modifier.weight(1f), cs)
            }
        }
    }
}

@Composable
fun ArcadeBarPro(label: String, percent: Int, color: Color, modifier: Modifier = Modifier, cs: ColorScheme) {
    val progress by animateFloatAsState(targetValue = percent / 100f, tween(1000))
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = cs.onSurfaceVariant)
            Text("$percent%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = color, trackColor = color.copy(alpha = 0.15f)
        )
    }
}

/* -------------------------------------------------------------------------- */
/* FORMS (Inputs e Containers)                                                */
/* -------------------------------------------------------------------------- */
@Composable
fun SectionTitlePro(title: String, icon: ImageVector, cs: ColorScheme) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 4.dp)) {
        Icon(icon, contentDescription = null, tint = cs.primary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, color = cs.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun ProfileFormContainer(cs: ColorScheme, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f)),
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), content = content)
    }
}

@Composable
fun ProfileInputPro(label: String, value: String, cs: ColorScheme, modifier: Modifier = Modifier, singleLine: Boolean = true, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label, color = cs.onSurfaceVariant) },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = cs.outline.copy(alpha = 0.15f),
            focusedBorderColor = cs.primary,
            unfocusedContainerColor = cs.surface,
            focusedContainerColor = cs.surface
        )
    )
}

/* -------------------------------------------------------------------------- */
/* ACCOUNT STATUS                                                             */
/* -------------------------------------------------------------------------- */
@Composable
fun AccountStatusCardPro(isPremium: Boolean, onUpgrade: () -> Unit, onManage: () -> Unit, cs: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = if (isPremium) cs.primary.copy(alpha = 0.05f) else cs.surface,
        border = BorderStroke(1.dp, if (isPremium) cs.primary.copy(alpha = 0.3f) else cs.outline.copy(alpha = 0.1f)),
        tonalElevation = 1.dp
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isPremium) cs.primary else cs.outline.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = if (isPremium) cs.onPrimary else cs.onSurfaceVariant
                    )
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(if (isPremium) "Premium Member" else "Free Account", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = cs.onSurface)
                    Text(if (isPremium) "All features unlocked" else "Basic access only", fontSize = 13.sp, color = cs.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                BenefitRowPro("AI workout generation", isPremium, cs)
                BenefitRowPro("Detailed analytics & insights", isPremium, cs)
                BenefitRowPro("Priority support & exclusive plans", isPremium, cs)
            }

            Spacer(Modifier.height(24.dp))

            if (!isPremium) {
                val infiniteTransition = rememberInfiniteTransition()
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f, targetValue = 1.02f,
                    animationSpec = infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse)
                )
                Button(
                    onClick = onUpgrade,
                    modifier = Modifier.fillMaxWidth().height(52.dp).scale(scale),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cs.primary)
                ) {
                    Text("Upgrade to Premium", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            } else {
                OutlinedButton(
                    onClick = onManage,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.5f))
                ) {
                    Text("Manage Subscription", color = cs.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BenefitRowPro(text: String, isPremium: Boolean, cs: ColorScheme) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Rounded.CheckCircle,
            contentDescription = null,
            tint = if (isPremium) cs.primary else cs.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(text, color = cs.onSurface, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}