package org.fitverse.project.destinations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.components.background.PremiumGamifiedBackground
import com.example.presentation.screens.ui.community.AddNewPostScreen
import com.example.presentation.screens.ui.community.CommunityScreen
import com.example.presentation.screens.ui.community.viewmodel.CommunityViewModel
import com.example.presentation.screens.ui.dashboard.DashboardScreen
import org.koin.compose.koinInject

@Composable
fun AddPostDestination(toBack: () -> Unit) {

    Box(modifier = Modifier.fillMaxSize()){
        PremiumGamifiedBackground()
        AddNewPostScreen(
            onBackClick = {
                toBack()
            },
            onPostSuccess = { }
        )
    }
}