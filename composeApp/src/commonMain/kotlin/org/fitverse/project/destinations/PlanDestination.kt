package org.fitverse.project.destinations

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.plans.AppPlansScreen


@Composable
fun PlanDestination(navigateToProfile: () -> Unit) {
    com.example.presentation.screens.ui.plans.AppPlansScreen(
        navigateToProfile = { navigateToProfile() }
    )
}
