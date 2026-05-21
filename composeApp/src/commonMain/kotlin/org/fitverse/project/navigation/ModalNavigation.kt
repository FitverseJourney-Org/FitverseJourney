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
import androidx.compose.material.icons.automirrored.rounded.Assignment
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    val colors = MaterialTheme.colorScheme

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        scrimColor = Color.Black.copy(alpha = 0.85f), // Scrim denso para foco no menu
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.82f),
                // Usando o fundo absoluto do app (#0A0B0F)
                drawerContainerColor = colors.background,
                drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),
                drawerTonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    // --- HEADER: STATUS DO AVATAR ---
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            // Aura Neon Roxo (Primary)
                            Box(modifier = Modifier.size(90.dp).background(colors.primary.copy(alpha = 0.1f), CircleShape))

                            Surface(
                                modifier = Modifier.size(76.dp),
                                shape = CircleShape,
                                color = colors.surface,
                                border = BorderStroke(2.dp, colors.primary)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Rounded.Person, null, tint = colors.onSurface, modifier = Modifier.size(36.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "ALEX JOURNEY",
                            color = colors.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )

                        // Nível com Badge Azul (Secondary) para clareza técnica
                        Surface(
                            modifier = Modifier.padding(top = 8.dp),
                            color = colors.secondary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, colors.secondary.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "LEVEL 14",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = colors.secondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    // --- WIDGETS RÁPIDOS (ESTILO GLASS) ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionWidget(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.Bolt,
                            title = "Free Plan",
                            subtitle = "Upgrade",
                            accentColor = colors.primary, // Roxo para ações de "Energia/Upgrade"
                            onClick = { onNavigate(NavRoutes.PlanPayment) }
                        )
                        QuickActionWidget(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.GeneratingTokens,
                            title = "Referrals",
                            subtitle = "Earn Pts",
                            accentColor = colors.secondary, // Azul para utilitários
                            onClick = { onNavigate(NavRoutes.PlanPayment) }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- SEÇÕES DE MENU ---
                    MenuSectionTitle("EXPLORE")
                    MenuCardContainer {
                        ActionRow("Profile", Icons.Rounded.Person, colors) { onNavigate(NavRoutes.HomeFlow.Profile) }
                        ActionRow("Workout Plan", Icons.Rounded.FitnessCenter, colors) { onNavigate(NavRoutes.PlanWorkoutFlow) }
                        ActionRow("Historic", Icons.Rounded.History, colors) { onNavigate(NavRoutes.Historic) }
                        ActionRow("Achievements", Icons.Rounded.Star, colors) { onNavigate(NavRoutes.Achievements) }
                        ActionRow("Progress", Icons.Rounded.Timeline, colors) { onNavigate(NavRoutes.Progress) }
                        ActionRow("Leaderboards", Icons.Rounded.EmojiEvents, colors) { onNavigate(NavRoutes.Leaderboards) }
                        ActionRow("Friends", Icons.Rounded.Group, colors) { onNavigate(NavRoutes.Friends) }
                        ActionRow("Tasks", Icons.AutoMirrored.Rounded.Assignment, colors) { onNavigate(NavRoutes.TasksFlow) }
                        ActionRow("Shop", Icons.Rounded.ShoppingCart, colors) { onNavigate(NavRoutes.Shopping) }
                        ActionRow("Wiki Fitness", Icons.Rounded.Info, colors) { onNavigate(NavRoutes.WikiFitness) }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    MenuSectionTitle("SETTINGS")
                    MenuCardContainer {
                        ActionRow("Devices", Icons.Rounded.Devices, colors) { onNavigate(NavRoutes.Devices) }
                        ActionRow("Help", Icons.AutoMirrored.Rounded.Help, colors) { onNavigate(NavRoutes.HelpSupport) }
                        ActionRow("Language", Icons.Rounded.Language, colors) { onNavigate(NavRoutes.Language) }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Logout em Vermelho (Error)
                    ActionRow("Logout",
                        Icons.AutoMirrored.Rounded.Logout, colors, isDanger = true, onClick = onLogout)

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        },
        content = content
    )
}

@Composable
private fun MenuSectionTitle(title: String) {
    val colors = MaterialTheme.colorScheme
    Text(
        text = title.uppercase(),
        color = colors.onSurfaceVariant.copy(alpha = 0.6f),
        fontSize = 11.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.5.sp,
        modifier = Modifier.padding(start = 12.dp, top = 24.dp, bottom = 8.dp)
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
    val colors = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(20.dp),
        // SurfaceVariant (#16171D) com transparência para o efeito de profundidade
        color = colors.surfaceVariant.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = accentColor, modifier = Modifier.size(22.dp))
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                color = colors.onSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle.uppercase(),
                color = accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
        }
    }
}
@Composable
fun MenuCardContainer(content: @Composable ColumnScope.() -> Unit) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface.copy(alpha = 0.5f))
            .border(0.5.dp, colors.outline.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            .padding(vertical = 4.dp),
        content = content
    )
}

@Composable
fun ActionRow(
    label: String,
    icon: ImageVector,
    colors: ColorScheme,
    isDanger: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor = if (isDanger) colors.error else colors.onSurface
    val iconColor = if (isDanger) colors.error else colors.primary // Ícones em Roxo para destaque

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .clip(RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.Rounded.ChevronRight, null, tint = colors.onSurfaceVariant.copy(alpha = 0.3f), modifier = Modifier.size(16.dp))
    }
}