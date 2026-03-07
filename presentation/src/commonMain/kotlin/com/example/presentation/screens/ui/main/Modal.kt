// ModalDrawer.kt
package com.example.presentation.screens.ui.main

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.main.profile.ActionRow

/**
 * Modal drawer styled with app color scheme.
 *
 * - drawerState: managed externally (ex: rememberDrawerState)
 * - onNavigate: callback to navigate
 * - onLogout: logout action
 * - content: main screen content
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDrawerSheetMainScreen(
    drawerState: DrawerState,
    content: @Composable () -> Unit,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = cs.surfaceVariant
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                ) {

                    // Header
                    Text(
                        text = "Fitverse Journey",
                        color = cs.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Navigation section (primary)
                    DrawerSection(title = "") {
                        ActionRow(
                            label = "Home",
                            icon = Icons.Default.Home,
                            onClick = { onNavigate("home") }
                        )
                        ActionRow(
                            label = "Workouts",
                            icon = Icons.Default.FitnessCenter,
                            onClick = { onNavigate("workouts") }
                        )
                        ActionRow(
                            label = "Nutrition",
                            icon = Icons.Default.LocalDining,
                            onClick = { onNavigate("nutrition") }
                        )
                        ActionRow(
                            label = "Profile",
                            icon = Icons.Default.Person,
                            onClick = { onNavigate("profile") }
                        )
                        ActionRow(
                            label = "Achievements",
                            icon = Icons.Default.Star,
                            onClick = { onNavigate("achievements") }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerSection(title = "Preferences") {
                        ActionRow(
                            label = "Settings",
                            icon = Icons.Default.Settings,
                            onClick = { onNavigate("settings") }
                        )
                        ActionRow(
                            label = "Help & Support",
                            icon = Icons.Default.Help,
                            onClick = { onNavigate("help") }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerSection(title = "Account") {
                        ActionRow(
                            label = "Logout",
                            icon = Icons.Default.Logout,
                            danger = true,
                            onClick = onLogout
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        },
        content = content
    )
}

/* ---------- Helpers and small components ---------- */

/**
 * Single drawer item helper — not used directly in the sample above but kept available.
 * Uses app color scheme for selection visuals.
 */
@Composable
private fun DrawerItem(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    NavigationDrawerItem(
        icon = icon,
        label = { Text(label) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(vertical = 4.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = cs.surface.copy(alpha = 0.12f),
            selectedTextColor = cs.primary,
            selectedIconColor = cs.primary,
            unselectedTextColor = cs.onSurface.copy(alpha = 0.9f),
            unselectedIconColor = cs.onSurface.copy(alpha = 0.9f)
        )
    )
}

@Composable
fun DrawerSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cs = MaterialTheme.colorScheme
    Column {
        if (title.isNotBlank()) {
            Text(
                text = title.uppercase(),
                color = cs.onSurface.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp, top = 18.dp, bottom = 8.dp)
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
            shape = RoundedCornerShape(14.dp),
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun DrawerSectionTitle(text: String) {
    val cs = MaterialTheme.colorScheme
    Text(
        text = text,
        color = cs.onSurface.copy(alpha = 0.6f),
        modifier = Modifier.padding(top = 8.dp, bottom = 6.dp),
        fontSize = 12.sp
    )
}

@Composable
private fun UpgradePill(onUpgrade: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    // subtle pulse animation
    val infinite = rememberInfiniteTransition()
    val scale = infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                1f at 0 with FastOutSlowInEasing
                1.03f at 300
                1f at 800
            }
        )
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(cs.primary.copy(alpha = 0.18f), cs.primary.copy(alpha = 0.07f))
                )
            )
            .clickable { onUpgrade() }
            .scale(scale.value)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "Upgrade", tint = cs.primary, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Upgrade", color = cs.primary, fontSize = 12.sp)
        }
    }
}

private fun initials(name: String?): String {
    if (name.isNullOrBlank()) return ""
    return name.trim().split("\\s+".toRegex()).mapNotNull { it.firstOrNull()?.toString()?.uppercase() }.take(2).joinToString("")
}

/** Simple app info holder */
private object AppInfo {
    const val version = "1.0.0"
}