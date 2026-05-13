package org.fitverse.project.destinations.community

import androidx.compose.runtime.Composable
import com.example.presentation.ui.community.CommunityRoot
import com.example.presentation.ui.community.viewmodel.CommunityViewModel

@Composable
fun CommunityDestination(
    viewModel:          CommunityViewModel,
    toAddPost:          () -> Unit,
    onSheetStateChange: (Boolean) -> Unit = {},
) {
    CommunityRoot(
        viewModel          = viewModel,
        toAddPost          = toAddPost,
        onSheetStateChange = onSheetStateChange,
    )
}
