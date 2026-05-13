package org.fitverse.project.destinations.dashboad

import androidx.compose.runtime.Composable
import com.example.presentation.ui.notification.NotificationRoot
import com.example.presentation.ui.notification.NotificationViewModel

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
