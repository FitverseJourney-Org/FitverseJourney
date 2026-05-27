package org.fitverse.project.destinations.payments

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import org.fitverse.presentation.ui.plan.FreePlanScreen

@Composable
fun PlanPaymentDestination(
    toBack   : () -> NavKey?,
    toTrial  : () -> Unit,
    modifier : Modifier,
) {
    FreePlanScreen(
        onBack   = { toBack() },
        onTrial  = toTrial,
        modifier = modifier,
    )
}