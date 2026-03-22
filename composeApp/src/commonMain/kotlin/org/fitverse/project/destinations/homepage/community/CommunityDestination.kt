package org.fitverse.project.destinations.homepage.community

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.community.CommunityScreen
import com.example.presentation.screens.ui.community.viewmodel.CommunityViewModel
import org.koin.compose.koinInject

@Composable
fun CommunityDestination(
    toAddPost: () -> Unit
) {

    val viewmodel = koinInject<CommunityViewModel>()

    Box(modifier = Modifier.fillMaxSize()){
        CommunityScreen(
            viewModel = viewmodel,
            toAddPost = toAddPost

        )
    }
}