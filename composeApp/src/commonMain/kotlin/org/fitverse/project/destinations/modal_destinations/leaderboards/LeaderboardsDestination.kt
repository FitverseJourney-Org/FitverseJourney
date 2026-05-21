package org.fitverse.project.destinations.modal_destinations.leaderboards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.fitverse.presentation.ui.leaderboards.LeaderboardRoot
import org.fitverse.presentation.ui.leaderboards.viewmodel.LeaderboardsViewModel

@Composable
fun LeaderboardsDestination(
    viewModel:    LeaderboardsViewModel,
    navigateBack: () -> Unit,
    modifier:     Modifier,
) {
    LeaderboardRoot(
        viewModel = viewModel,
        onBack    = navigateBack,
        modifier  = modifier,
    )
}
