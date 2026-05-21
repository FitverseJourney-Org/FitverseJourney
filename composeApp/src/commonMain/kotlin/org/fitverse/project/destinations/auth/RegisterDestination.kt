package org.fitverse.project.destinations.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.fitverse.presentation.ui.authentication.register.RegisterScreen
import org.fitverse.presentation.ui.authentication.register.RegisterViewModel
import org.koin.compose.koinInject

@Composable
fun RegisterDestination(onBack: () -> Unit) {
    val viewModel = koinInject<RegisterViewModel>()
    val state by viewModel.uiState.collectAsState()

    RegisterScreen(
        state = state,
        onAction = viewModel::onIntent,
        onBack = onBack,
    )
}
