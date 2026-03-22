package org.fitverse.project.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import org.fitverse.project.routes.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDrawerSheetMainScreen(
    drawerState: DrawerState,
    content: @Composable () -> Unit,
    gesturesEnabled: Boolean = false,
    onNavigate: (backStackEntry: NavKey) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        // Scrim mais denso para focar totalmente no menu
        scrimColor = Color.Black.copy(alpha = 0.85f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.85f), // Reduzi levemente para ver o fundo
                drawerContainerColor = cs.background, // Fundo ultra escuro (#0A0A0E)
                drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp) // Cantos modernos
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState())
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    // --- HEADER: IDENTIDADE ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            // Círculo de brilho externo (Glow sutil)
                            Box(
                                modifier = Modifier
                                    .size(92.dp)
                                    .background(cs.primary.copy(alpha = 0.05f), CircleShape)
                            )

                            // Avatar
                            Surface(
                                modifier = Modifier.size(80.dp),
                                shape = CircleShape,
                                color = cs.surface,
                                border = BorderStroke(2.dp, cs.primary) // Neon Volt
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Rounded.Person,
                                        contentDescription = null,
                                        tint = cs.onSurface,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "ALEX JOURNEY",
                            color = cs.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )

                        // Badge de Nível (Roxo Elétrico para contraste com o Neon)
                        Surface(
                            modifier = Modifier.padding(top = 8.dp),
                            color = cs.secondary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, cs.secondary.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "LEVEL 14",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = cs.secondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // --- WIDGETS RÁPIDOS ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionWidget(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.CreditCard,
                            title = "Free Plan",
                            subtitle = "Upgrade",
                            accentColor = cs.primary,
                            onClick = {
                                onNavigate(NavRoutes.PlanPaymentScreen)
                            }
                        )
                        QuickActionWidget(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.PersonAdd,
                            title = "Referrals",
                            subtitle = "Earn Pts",
                            accentColor = cs.tertiary, // Dourado
                            onClick = {
                                onNavigate(NavRoutes.PlanPaymentScreen)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- MENU PRINCIPAL ---
                    Text(
                        text = "EXPLORE",
                        color = cs.onSurface.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
                    )

                    // Lista de Itens do Menu
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(cs.surface.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                            .padding(vertical = 8.dp)
                    ) {
                        ActionRow("Workout Plan", Icons.Rounded.FitnessCenter) { onNavigate(NavRoutes.PlanWorkoutFlow) }
                        ActionRow("Meals Plan", Icons.Rounded.Fastfood) { onNavigate(NavRoutes.PlanMealsFlow) }
                        ActionRow("Historic", Icons.Rounded.History) { onNavigate(NavRoutes.Historic) }
                        ActionRow("Leaderboards", Icons.Rounded.Leaderboard) { onNavigate(NavRoutes.Leaderboards) }
                        ActionRow("Tasks Flow", Icons.Rounded.Assignment) { onNavigate(NavRoutes.TasksFlow) }
                        ActionRow("Friends", Icons.Rounded.People) { onNavigate(NavRoutes.Friends) }
                        ActionRow("Progress", Icons.Rounded.Analytics) { onNavigate(NavRoutes.Progress) }
                        ActionRow("Achievements", Icons.Rounded.Star) { onNavigate(NavRoutes.Achievements) }
                    }

                    Text(
                        text = "SETTINGS",
                        color = cs.onSurface.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
                    )

                    // Lista de Itens do Menu
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(cs.surface.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                            .padding(vertical = 8.dp)
                    ) {
                        ActionRow("Devices", Icons.Rounded.Devices) { onNavigate(NavRoutes.Devices) }
                        ActionRow("Help & Support", Icons.Rounded.Help) { onNavigate(NavRoutes.HelpSupport) }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- FOOTER / LOGOUT ---
                    ActionRow(
                        label = "Logout",
                        icon = Icons.Rounded.Logout,
                        isDanger = true,
                        onClick = onLogout
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        },
        content = content
    )
}

@Composable
fun QuickActionWidget(
    modifier: Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(20.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = accentColor, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(8.dp))
            Text(title, color = cs.onBackground, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = accentColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ActionRow(
    label: String,
    icon: ImageVector,
    isDanger: Boolean = false,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val contentColor = if (isDanger) cs.error else cs.onBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDanger) cs.error else cs.primary, // Ícones sempre Neon para guiar o olho
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = contentColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        if (!isDanger) {
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = cs.outline,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}