package org.fitverse.fitverseJourney.routes.trial

import androidx.compose.runtime.Composable
import com.example.presentation.screens.trial.TrialIntroScreen

@Composable
fun TrialIntroRoute(
    onStartTrial: () -> Unit,
    onSkip: () -> Unit
){
    TrialIntroScreen(
        onStartTrial = onStartTrial,
        onSkip = onSkip
    )
}