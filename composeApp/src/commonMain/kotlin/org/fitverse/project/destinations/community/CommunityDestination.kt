package org.fitverse.project.destinations.community

import androidx.compose.runtime.Composable
import org.fitverse.presentation.ui.community.CommunityRoot
import org.fitverse.presentation.ui.community.viewmodel.CommunityViewModel

@Composable
fun CommunityDestination(
    viewModel:          CommunityViewModel,
    toAddPost:          () -> Unit,
    toGroupHome:        (String) -> Unit,
    onSheetStateChange: (Boolean) -> Unit = {},
) {
    CommunityRoot(
        viewModel          = viewModel,
        toAddPost          = toAddPost,
        toGroupHome        = toGroupHome,
        onSheetStateChange = onSheetStateChange,
    )
}
