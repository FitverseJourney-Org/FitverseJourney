package org.fitverse.project.destinations.modal_destinations.helpSupport

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.fitverse.presentation.ui.helpSupport.SupportScreen

@Composable
fun HelpSupportDestination(toBack: () -> Unit, modifier: Modifier) {
    SupportScreen(
        onBack = toBack,
        modifier = modifier
    )
}
