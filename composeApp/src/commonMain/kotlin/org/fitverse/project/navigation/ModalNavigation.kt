package org.fitverse.project.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.presentation.screens.ui.main.profile.ActionRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDrawerSheetMainScreen(
    drawerState: DrawerState,
    content: @Composable () -> Unit,
    onNavigate: (backStackEntry: NavKey) -> Unit = {},
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
                            onClick = { onNavigate(NavRoutes.Devices) }
                        )
                        ActionRow(
                            label = "Workouts",
                            icon = Icons.Default.FitnessCenter,
                            onClick = { onNavigate(NavRoutes.Devices) }
                        )
                        ActionRow(
                            label = "Nutrition",
                            icon = Icons.Default.LocalDining,
                            onClick = { onNavigate(NavRoutes.Devices) }
                        )
                        ActionRow(
                            label = "Profile",
                            icon = Icons.Default.Person,
                            onClick = {onNavigate(NavRoutes.Devices) }
                        )
                        ActionRow(
                            label = "Achievements",
                            icon = Icons.Default.Star,
                            onClick = { onNavigate(NavRoutes.Devices) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerSection(title = "Preferences") {
                        ActionRow(
                            label = "Medidores e Dispositivos",
                            icon = Icons.Default.Settings,
                            onClick = { onNavigate(NavRoutes.Devices) }
                        )
                        ActionRow(
                            label = "Help & Support",
                            icon = Icons.Default.Help,
                            onClick = { onNavigate(NavRoutes.Devices) }
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