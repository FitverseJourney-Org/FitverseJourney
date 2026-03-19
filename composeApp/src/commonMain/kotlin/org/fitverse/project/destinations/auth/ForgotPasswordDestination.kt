package org.fitverse.project.destinations.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.presentation.components.snackbar.AppSnackbarHost
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.screens.ui.authentication.login.components.AnimatedLoginBackground
import com.example.presentation.screens.ui.authentication.resetPassword.ResetPasswordScreen
import com.example.presentation.screens.ui.authentication.resetPassword.viewmodel.ResetPasswordViewModel
import org.koin.compose.koinInject

@Composable
fun ResetPasswordDestination(toLogin: () -> Unit) {
    val viewmodel = koinInject<ResetPasswordViewModel>()
    val state by viewmodel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.snackBarData) {
        state.snackBarData?.let {
            snackBarHostState.showSnackbar(
                message = it.message,
                withDismissAction = true
            )
            viewmodel.cleanSnackBar()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedLoginBackground()
        ResetPasswordScreen(
            state = state,
            snackBarHostState = {
                AppSnackbarHost(
                    snackbarHostState = snackBarHostState,
                    snackbarType = state.snackBarData?.type ?: SnackbarType.INFO
                )
            },
            onBackClick = {
                toLogin()
            },
            isLoading = false,
            onEmailChange = viewmodel::onEmailChange,
            onSendResetLink = {
                keyboardController?.hide()
                viewmodel.onClickResetPassword()

            },
            emailErrors = emptyList(),
            onNavigateToLogin = {
                toLogin()
            }
        )
    }
}