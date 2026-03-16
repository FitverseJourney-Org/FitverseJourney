package org.fitverse.project.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    gesturesEnabled: Boolean = true,
    onNavigate: (backStackEntry: NavKey) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
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
                    FitVerseDrawerHeader(
                        userName = "Alex Journey",
                        level = "Level 14",
                        xpProgress = 0.65f
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Navigation section (primary)
                    DrawerSection(title = "Usuário") {
                        ActionRow(
                            label = "Plan Workout",
                            icon = Icons.Default.FitnessCenter,
                            onClick = { onNavigate(NavRoutes.PlanWorkoutFlow) } // Lembre de atualizar as rotas depois!
                        )
                        ActionRow(
                            label = "Plan Nutrition",
                            icon = Icons.Default.LocalDining,
                            onClick = { onNavigate(NavRoutes.ActionsNutrition) }
                        )
                        ActionRow(
                            label = "Tasks",
                            icon = Icons.Default.Assignment, // Ícone de prancheta/lista de tarefas
                            onClick = { onNavigate(NavRoutes.ActionsTasks) }
                        )
                        ActionRow(
                            label = "Friends",
                            icon = Icons.Default.People, // Ícone de grupo de pessoas
                            onClick = { onNavigate(NavRoutes.ActionsFriends) }
                        )
                        ActionRow(
                            label = "Leaderboards",
                            icon = Icons.Default.Leaderboard, // Ícone de pódio com gráfico
                            onClick = { onNavigate(NavRoutes.ActionsLeaderboards) }
                        )
                        ActionRow(
                            label = "Historic",
                            icon = Icons.Default.History, // Ícone de relógio voltando no tempo
                            onClick = { onNavigate(NavRoutes.ActionsHistoric) }
                        )
                        ActionRow(
                            label = "Achievements",
                            icon = Icons.Default.EmojiEvents, // Ícone de troféu (mais imersivo que a estrela)
                            onClick = { onNavigate(NavRoutes.ActionsAchievements) }
                        )
                        ActionRow(
                            label = "Account",
                            icon = Icons.Default.Devices, // Ícone de celular/tablet (ou use Icons.Default.Watch para smartwatches)
                            onClick = { onNavigate(NavRoutes.PreferencesDevicesConnect) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerSection(title = "Preferences") {
                        ActionRow(
                            label = "Unidades",
                            icon = Icons.Default.Devices, // Ícone de celular/tablet (ou use Icons.Default.Watch para smartwatches)
                            onClick = { onNavigate(NavRoutes.PreferencesDevicesConnect) }
                        )
                        ActionRow(
                            label = "Idiomas",
                            icon = Icons.Default.Devices, // Ícone de celular/tablet (ou use Icons.Default.Watch para smartwatches)
                            onClick = { onNavigate(NavRoutes.PreferencesDevicesConnect) }
                        )
                        ActionRow(
                            label = "Medidores e Dispositivos",
                            icon = Icons.Default.Devices, // Ícone de celular/tablet (ou use Icons.Default.Watch para smartwatches)
                            onClick = { onNavigate(NavRoutes.PreferencesDevicesConnect) }
                        )

                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerSection(title = "Support") {
                        ActionRow(
                            label = "Contact Us",
                            icon = Icons.Default.Help,
                            onClick = { onNavigate(NavRoutes.PreferencesHelpSupport) }
                        )
                        ActionRow(
                            label = "Perguntas Frequentes",
                            icon = Icons.Default.Help,
                            onClick = { onNavigate(NavRoutes.PreferencesHelpSupport) }
                        )
                    }

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
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = cs.onSurface.copy(alpha = 0.03f)),
                modifier = Modifier.size(40.dp)
            ) {

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )

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
fun FitVerseDrawerHeader(
    userName: String,
    level: String,
    xpProgress: Float
) {
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar Placeholder
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(cs.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = cs.primary)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurface
                )
                Text(
                    text = level,
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Barra de XP
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { xpProgress },
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape),
                color = cs.primary,
                trackColor = cs.onSurfaceVariant.copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${(xpProgress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = cs.onSurfaceVariant
            )
        }
    }
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