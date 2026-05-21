package org.fitverse.project.destinations.modal_destinations.device

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import org.fitverse.presentation.ui.device.DevicesScreen

@Composable
fun DevicesDestination(toBack: () -> NavKey?, modifier: Modifier) {
    DevicesScreen(
        onBack = { toBack() },
        modifier = modifier
    )
}
