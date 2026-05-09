package org.fitverse.project.destinations.homepage.community

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.community.CommunityScreen
import com.example.presentation.widgets.DarkGamifiedDashboardBackground

@Composable
fun CommunityDestination(
    toAddPost: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()){
        DarkGamifiedDashboardBackground()
        CommunityScreen()
    }
}