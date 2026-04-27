package org.fitverse.project.destinations.trial

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.screens.ui.trial.TrialIntroScreen
import com.example.presentation.screens.ui.trial.event.TrialEvent
import com.example.presentation.screens.ui.trial.viewmodel.TrialViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun TrialDestination(
    toDashboard : () -> Unit,
    toLogin     : () -> Unit,
) {
    val viewModel       = koinInject<TrialViewModel>()
    val state           by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHost    = remember { SnackbarHostState() }

    // ── Eventos one-shot ──────────────────────────────────────────────────────
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is TrialEvent.NavigateToDashboard      -> toDashboard()
                is TrialEvent.NavigateToLogin          -> toLogin()
                is TrialEvent.ShowSnackbar          -> snackbarHost.showSnackbar(event.message)
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