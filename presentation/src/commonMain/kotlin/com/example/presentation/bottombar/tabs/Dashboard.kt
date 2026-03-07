package com.example.presentation.bottombar.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.presentation.components.background.PremiumGamifiedBackground
import com.example.presentation.screens.ui.main.dashboard.DashboardScreen
import org.jetbrains.compose.resources.stringResource

object DashboardTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Dashboard",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Scaffold(
            modifier = Modifier.fillMaxSize().navigationBarsPadding(),
            bottomBar = {
                NavigationBar {
                    Text("Dashboard")
                }
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .navigationBarsPadding()
            ) {
                PremiumGamifiedBackground()
                DashboardScreen(
                    modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                    username = "Athlete",
                    avatarInitials = "A",
                    exit = {},
                    onNotificationsClick = {},
                )
            }
        }
    }
}