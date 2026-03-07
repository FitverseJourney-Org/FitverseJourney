package org.fitverse.project.navigation.navRoutesFlows

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.presentation.screens.ui.main.ModalDrawerSheetMainScreen
import org.fitverse.project.navigation.ContentFlowApp
import org.fitverse.project.navigation.NavRoutes

@Composable
fun HomeFlow(
    config: SavedStateConfiguration,
    start: NavKey = NavRoutes.DashboardScreen,
    onSignOut: () -> Unit
) {
    val homeBackStack = rememberNavBackStack(config, start)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val currentRoute = homeBackStack.lastOrNull()

    val tabs = listOf(
        NavRoutes.DashboardScreen,
        NavRoutes.WorkoutScreen,
        NavRoutes.NutritionScreen,
        NavRoutes.ProfileScreen
    )

    // verifica se a rota atual pertence às tabs
    val isTabRoute = tabs.any { tab ->
        tab::class == currentRoute?.let { it::class }
    }

    if (isTabRoute) {
        ModalDrawerSheetMainScreen(
            drawerState = drawerState,
            content = {
                ContentFlowApp(
                    config = config,
                    start = start,
                    tabs = tabs,
                    homeBackStack = homeBackStack
                )
            }
        )
    } else {
        ContentFlowApp(
            config = config,
            start = start,
            tabs = tabs,
            homeBackStack = homeBackStack
        )
    }
}