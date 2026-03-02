package org.fitverse.fitverseJourney.routes.authentication

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.domain.model.authentication.register.RegisterAction
import com.example.domain.model.authentication.register.progress
import com.example.presentation.components.snackbar.AppSnackbarHost
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.screens.ui.authentication.register.RegisterViewModel
import com.example.presentation.screens.ui.authentication.register.RegisterScreen

@Composable
fun RegisterRoute(
    presenter: RegisterViewModel,
    onExitToLogin: () -> Unit
) {
    val state by presenter.state.collectAsState()
    val uiEvent by presenter.uiEvent.collectAsState(initial = null)
    val snackBarHostState = remember { SnackbarHostState() }

    val animatedProgress by animateFloatAsState(
        targetValue = state.page.progress(),
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "register-progress"
    )

    LaunchedEffect(uiEvent) {
        presenter.uiEvent.collect { event ->
            when(event){
                RegisterAction.Exit -> {
                    Log.d("RegisterRoute", "exit event: $uiEvent")
                    onExitToLogin()
                }
                RegisterAction.Finish -> {
                    Log.d("RegisterRoute", "finish event: $uiEvent")
                    onExitToLogin()
                }
                else -> {
                    Log.d("RegisterRoute", "Other event: $uiEvent")
                }
            }
        }
    }

    LaunchedEffect(state.snackBarData) {
        state.snackBarData?.let { event ->
            snackBarHostState.showSnackbar(
                message = event.message,
                withDismissAction = true
            )
            presenter.consumeSnackBar()
        }
    }

    RegisterScreen(
        state = state,
        onAction = presenter::onAction,
        animatedProgress = animatedProgress,
        snackBarHostState = {
            AppSnackbarHost(
                snackbarHostState = snackBarHostState,
                snackbarType = state.snackBarData?.type ?: SnackbarType.INFO
            )
        }
    )
}

