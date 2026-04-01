package org.fitverse.project.destinations.homepage.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.profile.ProfileScreen
import org.fitverse.project.destinations.homepage.dashboad.DarkGamifiedDashboardBackground

@Composable
fun ProfileDestination() {
    Box(modifier = Modifier.fillMaxSize()){
        DarkGamifiedDashboardBackground()
        ProfileScreen()
    }
}