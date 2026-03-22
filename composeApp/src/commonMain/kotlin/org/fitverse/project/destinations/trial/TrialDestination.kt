package org.fitverse.project.destinations.trial

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.trial.TrialIntroScreen

@Composable
fun TrialDestination(
    toLogin: () -> Unit,

) {
    TrialIntroScreen(
        onSkip = { toLogin() },
        onStartTrial = { toLogin() }
    )
}