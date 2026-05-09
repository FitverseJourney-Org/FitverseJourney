package org.fitverse.project.destinations.auth

import androidx.compose.runtime.Composable
import com.example.presentation.ui.authentication.resetPassword.ResetPasswordScreen
import com.example.presentation.ui.authentication.resetPassword.ResetPasswordViewModel
import com.example.presentation.ui.authentication.resetPassword.states.ResetPasswordState

@Composable
fun ResetPasswordDestination(
    viewmodel: ResetPasswordViewModel,
    state: ResetPasswordState,
    onNavigateBack: () -> Unit,
) {
    ResetPasswordScreen(
        state = state,
        viewmodel = viewmodel,
        onNavigateBack = onNavigateBack,
    )
}