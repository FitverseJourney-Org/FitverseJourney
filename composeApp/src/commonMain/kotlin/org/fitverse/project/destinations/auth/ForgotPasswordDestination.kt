package org.fitverse.project.destinations.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.fitverse.presentation.ui.authentication.resetPassword.ResetPasswordScreen
import org.fitverse.presentation.ui.authentication.resetPassword.ResetPasswordViewModel
import org.fitverse.presentation.ui.authentication.resetPassword.states.ResetPasswordNavigation
import org.koin.compose.koinInject

@Composable
fun ResetPasswordDestination(
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinInject<ResetPasswordViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationState.collect { event ->
            when (event) {
                ResetPasswordNavigation.NavigateBack -> onNavigateBack()
            }
        }
    }

    ResetPasswordScreen(
        state = state,
        viewmodel = viewModel,
        onNavigateBack = onNavigateBack,
    )
}
