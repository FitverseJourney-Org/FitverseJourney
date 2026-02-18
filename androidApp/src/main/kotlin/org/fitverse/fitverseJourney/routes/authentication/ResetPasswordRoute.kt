package org.fitverse.fitverseJourney.routes.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.presentation.components.snackbar.AppSnackbarHost
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.presenter.authentication.ResetPasswordPresenter
import com.example.presentation.screens.authentication.resetPassword.ResetPasswordScreen

@Composable
fun ResetPasswordRoute(
    presenter: ResetPasswordPresenter,
    onBackClick: () -> Unit,
) {
    val state by presenter.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.snackBarData) {
        state.snackBarData?.let {
            snackBarHostState.showSnackbar(
                message = it.message,
                withDismissAction = true
            )
            presenter.cleanSnackBar()
        }
    }

    ResetPasswordScreen(
        state = state,
        snackBarHostState = {
            AppSnackbarHost(
                snackbarHostState = snackBarHostState,
                snackbarType = state.snackBarData?.type ?: SnackbarType.INFO
            )
        },
        onBackClick = onBackClick,
        isLoading = false,
        onEmailChange = presenter::onEmailChange,
        onSendResetLink = {
            keyboardController?.hide()
            presenter.onClickResetPassword()

        },
        emailErrors = emptyList(),
        onNavigateToLogin = {
            onBackClick()
        }
    )
}