package org.fitverse.project.destinations.modal_destinations.achievement

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.fitverse.presentation.ui.achievements.AchievementsRoot
import org.fitverse.presentation.ui.achievements.viewmodel.AchievementsViewModel

@Composable
fun AchievementDestination(
    viewModel: AchievementsViewModel,
    toBack:    () -> Unit,
    modifier:  Modifier,
) {
    AchievementsRoot(
        viewModel = viewModel,
        onBack    = toBack,
        modifier  = modifier,
    )
}
