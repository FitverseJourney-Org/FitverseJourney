// ModalDrawer.kt
package org.fitverse.project.presentation.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgeDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.main.nutrition.TextPrimary
import com.example.presentation.screens.ui.main.nutrition.TextSecondary
import com.example.presentation.screens.ui.main.profile.ActionRow
import com.example.presentation.theme.DeepGreen
import com.example.presentation.theme.SurfaceGreen

private val DrawerBg = Color(0xFF0E2015)
private val DrawerSurface = Color(0xFF112718)
private val AccentGreen = Color(0xFF3FAE6A)
private val Muted = Color(0xFF9CB99A)
private val ItemSelectedBg = Color(0xFF133A29).copy(alpha = 0.6f)

/**
 * Professional Modal Drawer for Fitverse
 *
 * - drawerState: controlado externamente (ex: rememberDrawerState)
 * - currentRoute: para marcar item selecionado (pode ser null)
 * - onNavigate: callback para navegar (recebe String route)
 * - onLogout / onUpgrade: actions
 * - userName / userEmail / isPremium: display
 * - content: app content
 */
@Composable
fun ModalDrawerSheetMainScreen(
    drawerState: DrawerState,
    content: @Composable () -> Unit,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DeepGreen
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    // 🔰 Header simples
                    Text(
                        text = "Fitverse Journey",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    // 🧭 Navigation
                    _root_ide_package_.org.fitverse.project.presentation.ui.components.DrawerSection(
                        title = ""
                    ) {
                        ActionRow(
                            label = "Achievements",
                            icon = Icons.Default.Star,
                            onClick = { onNavigate("achievements") }
                        )
                        ActionRow(
                            label = "Progress",
                            icon = Icons.Default.Star,
                            onClick = { onNavigate("progress") }
                        )
                        ActionRow(
                            label = "Historic",
                            icon = Icons.Default.Star,
                            onClick = { onNavigate("historic") }
                        )
                        ActionRow(
                            label = "Friends",
                            icon = Icons.Default.Star,
                            onClick = { onNavigate("historic") }
                        )
                        ActionRow(
                            label = "My Tasks",
                            icon = Icons.Default.Star,
                            onClick = { onNavigate("historic") }
                        )
                        ActionRow(
                            label = "Workouts",
                            icon = Icons.Default.Star,
                            onClick = { onNavigate("historic") }
                        )
                    }

                    // ⚙️ Preferences
                    _root_ide_package_.org.fitverse.project.presentation.ui.components.DrawerSection(
                        title = "Preferences"
                    ) {
                        ActionRow(
                            label = "Language",
                            icon = Icons.Default.Settings,
                            onClick = { onNavigate("language") }
                        )
                        ActionRow(
                            label = "Settings",
                            icon = Icons.Default.Settings,
                            onClick = { onNavigate("language") }
                        )
                        ActionRow(
                            label = "Help & Support",
                            icon = Icons.Default.Help,
                            onClick = { onNavigate("help") }
                        )
                    }

                    // 🚪 Account
                    _root_ide_package_.org.fitverse.project.presentation.ui.components.DrawerSection(
                        title = "Account"
                    ) {
                        ActionRow(
                            label = "Logout",
                            icon = Icons.Default.Logout,
                            danger = true,
                            onClick = onLogout
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        },
        content = content
    )
}


/* ---------- Helpers and small components ---------- */

@Composable
private fun DrawerItem(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = icon,
        label = { Text(label) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(vertical = 4.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = _root_ide_package_.org.fitverse.project.presentation.ui.components.ItemSelectedBg,
            selectedTextColor = _root_ide_package_.org.fitverse.project.presentation.ui.components.AccentGreen,
            selectedIconColor = _root_ide_package_.org.fitverse.project.presentation.ui.components.AccentGreen,
            unselectedTextColor = Color.White.copy(alpha = 0.9f),
            unselectedIconColor = Color.White.copy(alpha = 0.9f)
        )
    )
}
@Composable
fun DrawerSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        Text(
            text = title.uppercase(),
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 12.dp, top = 18.dp, bottom = 8.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceGreen),
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
    Text(
        text = text,
        color = _root_ide_package_.org.fitverse.project.presentation.ui.components.Muted,
        modifier = Modifier.padding(top = 8.dp, bottom = 6.dp),
        fontSize = 12.sp
    )
}

@Composable
private fun UpgradePill(onUpgrade: () -> Unit) {
    // subtle pulse animation to call attention
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
            .background(Brush.horizontalGradient(listOf(_root_ide_package_.org.fitverse.project.presentation.ui.components.AccentGreen.copy(alpha = 0.18f), _root_ide_package_.org.fitverse.project.presentation.ui.components.AccentGreen.copy(alpha = 0.07f))))
            .clickable { onUpgrade() }
            .scale(scale.value)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "Upgrade", tint = _root_ide_package_.org.fitverse.project.presentation.ui.components.AccentGreen, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Upgrade", color = _root_ide_package_.org.fitverse.project.presentation.ui.components.AccentGreen, fontSize = 12.sp)
        }
    }
}

private fun initials(name: String?): String {
    if (name.isNullOrBlank()) return ""
    return name.trim().split("\\s+".toRegex()).mapNotNull { it.firstOrNull()?.toString()?.uppercase() }.take(2).joinToString("")
}

/** Simple app info holder, replace with build config if needed */
private object AppInfo {
    const val version = "1.0.0"
}
