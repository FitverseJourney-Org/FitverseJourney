package org.fitverse.project.navigation.destinations.auth

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import com.example.domain.model.authentication.register.RegisterAction
import com.example.domain.model.authentication.register.progress
import com.example.presentation.components.snackbar.AppSnackbarHost
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.navigationState.RegisterNavigation
import com.example.presentation.screens.ui.authentication.login.components.AnimatedLoginBackground
import com.example.presentation.screens.ui.authentication.register.RegisterScreen
import com.example.presentation.screens.ui.authentication.register.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun RegisterDestination(navigateToLogin: () -> Unit) {
    val viewmodel = koinInject<RegisterViewModel>()
    val state by viewmodel.state.collectAsState()
    val uiEvent by viewmodel.uiEvent.collectAsState(initial = null)
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
        viewmodel.uiEvent.collect { event ->
            when(event){
                RegisterAction.Exit -> {
                    println("valor EXIT")
                    navigateToLogin()
                }
                RegisterAction.Finish -> {
                    println("valor FINISH")
                    navigateToLogin()
                }
                else -> {

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
            viewmodel.consumeSnackBar()
        }
    }

    LaunchedEffect(viewmodel.navigationState){
        viewmodel.navigationState.collectLatest {
            when(it){
                is RegisterNavigation.ToLogin -> {
                    navigateToLogin()
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedLoginBackground()
        RegisterScreen(
            state = state,
            onAction = viewmodel::onAction,
            animatedProgress = animatedProgress,
            snackBarHostState = {
                AppSnackbarHost(
                    snackbarHostState = snackBarHostState,
                    snackbarType = state.snackBarData?.type ?: SnackbarType.INFO
                )
            }
        )
    }
}