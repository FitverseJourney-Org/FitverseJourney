package org.fitverse.project.destinations.modal_destinations.referrals

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.fitverse.presentation.ui.referrals.ReferralsScreen

@Composable
fun ReferralsDestination(
    onBack   : () -> Unit,
    modifier : Modifier,
) {
    ReferralsScreen(
        onBack   = onBack,
        modifier = modifier,
    )
}
