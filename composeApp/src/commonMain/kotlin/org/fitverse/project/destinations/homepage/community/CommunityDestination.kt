package org.fitverse.project.destinations.homepage.community

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.community.CommunityScreen
import org.fitverse.project.destinations.homepage.dashboad.DarkGamifiedDashboardBackground
import org.koin.compose.koinInject

@Composable
fun CommunityDestination(
    toAddPost: () -> Unit
) {

    //val viewmodel = koinInject<CommunityViewModel>()

    Box(modifier = Modifier.fillMaxSize()){
        DarkGamifiedDashboardBackground()
        CommunityScreen()
    }
}