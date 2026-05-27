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
        scrimColor = Color.Black.copy(alpha = 0.85f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.82f),
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
                    // --- HEADER ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .background(colors.primary.copy(alpha = 0.08f), CircleShape)
                            )
                            Surface(
                                modifier = Modifier.size(72.dp),
                                shape = CircleShape,
                                color = colors.surface,
                                border = BorderStroke(1.5.dp, colors.primary.copy(alpha = 0.6f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Rounded.Person,
                                        contentDescription = null,
                                        tint = colors.onSurface.copy(alpha = 0.7f),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "ALEX JOURNEY",
                            color = colors.onBackground,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Level + Rank chips inline
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                color = colors.secondary.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(0.5.dp, colors.secondary.copy(alpha = 0.35f))
                            ) {
                                Text(
                                    text = "LVL 14",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    color = colors.secondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Surface(
                                color = colors.primary.copy(alpha = 0.10f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(0.5.dp, colors.primary.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = "TITAN",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    color = colors.primary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // XP progress bar
                        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "XP",
                                    color = colors.onSurfaceVariant.copy(alpha = 0.45f),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "1,240 / 2,000",
                                    color = colors.onSurfaceVariant.copy(alpha = 0.45f),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            LinearProgressIndicator(
                                progress = { 0.62f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(50)),
                                color = colors.primary,
                                trackColor = colors.primary.copy(alpha = 0.12f)
                            )
                        }
                    }

                    // --- QUICK ACTIONS ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionWidget(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.Bolt,
                            title = "Free Plan",
                            subtitle = "Upgrade",
                            accentColor = colors.primary,
                            onClick = { onNavigate(NavRoutes.PlanPayment) }
                        )
                        QuickActionWidget(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.GeneratingTokens,
                            title = "Referrals",
                            subtitle = "Earn Pts",
                            accentColor = colors.secondary,
                            onClick = { onNavigate(NavRoutes.Referrals) }
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // --- EXPLORE ---
                    MenuSectionTitle("EXPLORE")
                    MenuCardContainer {
                        ActionRow("Profile",      Icons.Rounded.Person,          colors) { onNavigate(NavRoutes.HomeFlow.Profile) }
                        MenuDivider()
                        ActionRow("Workout Plan", Icons.Rounded.FitnessCenter,   colors) { onNavigate(NavRoutes.PlanWorkoutFlow) }
                        MenuDivider()
                        ActionRow("Historic",     Icons.Rounded.History,         colors) { onNavigate(NavRoutes.Historic) }
                        MenuDivider()
                        ActionRow("Achievements", Icons.Rounded.Star,            colors) { onNavigate(NavRoutes.Achievements) }
                        MenuDivider()
                        ActionRow("Progress",     Icons.Rounded.Timeline,        colors) { onNavigate(NavRoutes.Progress) }
                        MenuDivider()
                        ActionRow("Leaderboards", Icons.Rounded.EmojiEvents,     colors) { onNavigate(NavRoutes.Leaderboards) }
                        MenuDivider()
                        ActionRow("Friends",      Icons.Rounded.Group,           colors) { onNavigate(NavRoutes.Friends) }
                        MenuDivider()
                        ActionRow("Tasks",        Icons.AutoMirrored.Rounded.Assignment, colors) { onNavigate(NavRoutes.TasksFlow) }
                        MenuDivider()
                        ActionRow("Shop",         Icons.Rounded.ShoppingCart,    colors) { onNavigate(NavRoutes.Shopping) }
                        MenuDivider()
                        ActionRow("Wiki Fitness", Icons.Rounded.Info,            colors) { onNavigate(NavRoutes.WikiFitness) }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- SETTINGS ---
                    MenuSectionTitle("SETTINGS")
                    MenuCardContainer {
                        ActionRow("Devices",  Icons.Rounded.Devices,              colors) { onNavigate(NavRoutes.Devices) }
                        MenuDivider()
                        ActionRow("Help",     Icons.AutoMirrored.Rounded.Help,    colors) { onNavigate(NavRoutes.HelpSupport) }
                        MenuDivider()
                        ActionRow("Language", Icons.Rounded.Language,             colors) { onNavigate(NavRoutes.Language) }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- LOGOUT ---
                    Surface(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = colors.error.copy(alpha = 0.07f),
                        border = BorderStroke(0.5.dp, colors.error.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(colors.error.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.Logout,
                                    contentDescription = null,
                                    tint = colors.error,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Logout",
                                color = colors.error,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

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
    Row(
        modifier = Modifier.padding(start = 4.dp, top = 20.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(10.dp)
                .background(colors.primary.copy(alpha = 0.5f), RoundedCornerShape(1.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            color = colors.onSurfaceVariant.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.8.sp
        )
    }
}

@Composable
private fun MenuDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 58.dp, end = 4.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.07f)
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
        modifier = modifier.height(92.dp),
        shape = RoundedCornerShape(18.dp),
        color = colors.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(0.5.dp, accentColor.copy(alpha = 0.18f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(accentColor.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = title,
                color = colors.onSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle.uppercase(),
                color = accentColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.6.sp
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
            .clip(RoundedCornerShape(20.dp))
            .background(colors.surface.copy(alpha = 0.45f))
            .border(0.5.dp, colors.outline.copy(alpha = 0.08f), RoundedCornerShape(20.dp)),
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
    val iconColor    = if (isDanger) colors.error else colors.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(iconColor.copy(alpha = 0.10f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(17.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            color = contentColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = colors.onSurfaceVariant.copy(alpha = 0.2f),
            modifier = Modifier.size(14.dp)
        )
    }
}
