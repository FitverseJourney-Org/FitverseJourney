package org.fitverse.project.destinations.dashboad

import androidx.compose.runtime.Composable
import org.fitverse.presentation.ui.notification.NotificationRoot
import org.fitverse.presentation.ui.notification.NotificationViewModel

@Composable
fun NotificationDestination(
    viewModel:   NotificationViewModel,
    toDashboard: () -> Unit,
) {
    NotificationRoot(
        viewModel = viewModel,
        onBack    = toDashboard,
    )
}
