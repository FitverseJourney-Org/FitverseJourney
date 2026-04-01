package org.fitverse.project.destinations.payments

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.plans.AppPlansScreen


@Composable
fun PlanDestination(toBack: () -> Unit) {
    AppPlansScreen(
        onBack = { toBack() }
    )
}
