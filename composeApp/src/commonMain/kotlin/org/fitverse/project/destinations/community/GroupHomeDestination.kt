package org.fitverse.project.destinations.community

import androidx.compose.runtime.Composable
import org.fitverse.presentation.ui.community.GroupHomeRoot
import org.fitverse.presentation.ui.community.viewmodel.GroupHomeViewModel

@Composable
fun GroupHomeDestination(
    groupName: String,
    viewModel: GroupHomeViewModel,
    toBack: () -> Unit,
) {
    GroupHomeRoot(
        groupName = groupName,
        viewModel = viewModel,
        toBack    = toBack,
    )
}
