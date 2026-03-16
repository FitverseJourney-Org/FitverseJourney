package org.fitverse.project.destinations

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.main.plans.AppPlansScreen


@Composable
fun PlanDestination(navigateToProfile: () -> Unit) {
    AppPlansScreen(
        navigateToProfile = { navigateToProfile() }
    )
}
