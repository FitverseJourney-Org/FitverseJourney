package org.fitverse.project.destinations.trial

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.fitverse.presentation.ui.trial.TrialIntroScreen
import org.fitverse.presentation.ui.trial.event.TrialEvent
import org.fitverse.presentation.ui.trial.viewmodel.TrialViewModel

@Composable
fun TrialDestination(
    viewModel: TrialViewModel,
    onNavigateToDashboard: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Eventos one-shot do Canal
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                TrialEvent.NavigateToDashboard -> onNavigateToDashboard()
                TrialEvent.NavigateToLogin     -> onNavigateToLogin()
                is TrialEvent.ShowSnackbar     -> { /* mostrar snackbar */ }
            }
        }
    }

    TrialIntroScreen(
        state      = state,
        onActivate = viewModel::onActivate,
        onDismiss  = viewModel::onDismiss,
        onSelect   = viewModel::onPlanSelected,
    )
}