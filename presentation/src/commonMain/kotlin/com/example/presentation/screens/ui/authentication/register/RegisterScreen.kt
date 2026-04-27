package com.example.presentation.screens.ui.authentication.register

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.models.auth.register.RegisterIntent
import com.example.domain.models.auth.register.RegisterStep
import com.example.presentation.screens.ui.authentication.register.components.RegisterBackButton
import com.example.presentation.screens.ui.authentication.register.components.RegisterStepBar
import com.example.presentation.screens.ui.authentication.register.pages.ClasseStep
import com.example.presentation.screens.ui.authentication.register.pages.ContaStep
import com.example.presentation.screens.ui.authentication.register.pages.ObjetivosStep
import com.example.presentation.screens.ui.authentication.register.pages.PerfilStep
import com.example.domain.models.auth.register.RegisterUiState
import com.example.presentation.theme.RegisterDimens
import com.example.presentation.utils.getLocaleDatePattern
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onBack: () -> Unit = {},
    viewModel: RegisterViewModel,
) {
    val pattern      = getLocaleDatePattern()
    val state        by viewModel.uiState.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }
    val scope        = rememberCoroutineScope()

    LaunchedEffect(pattern) { viewModel.onLocaleResolved(pattern) }

    LaunchedEffect(state.registrationComplete, state.registrationCancellable) {
        if (state.registrationComplete || state.registrationCancellable) onBack()
    }

    // ── Consome o evento de snackbar ──────────────────────────────────────
    LaunchedEffect(state.snackbarEvent) {
        state.snackbarEvent?.let { event ->
            scope.launch {
                snackbarHost.showSnackbar(
                    message  = event.message,
                    duration = SnackbarDuration.Short,
                )
            }
            viewModel.onSnackbarConsumed()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHost) { data ->
                Snackbar(
                    snackbarData     = data,
                    containerColor   = if (state.snackbarEvent?.isError == true)
                        MaterialTheme.colorScheme.errorContainer
                    else
                        MaterialTheme.colorScheme.primaryContainer,
                    contentColor     = if (state.snackbarEvent?.isError == true)
                        MaterialTheme.colorScheme.onErrorContainer
                    else
                        MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        },
        containerColor = Color.Transparent,
    ) { _ ->
        RegisterScreenContent(state = state, onIntent = viewModel::onIntent)
    }
}

@Composable
private fun RegisterScreenContent(
    state: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val type   = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .systemBarsPadding(),
    ) {
        // ── Top bar ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start  = (RegisterDimens.screenHorizontal - 12.dp),
                    end    = RegisterDimens.screenHorizontal,
                    top    = 8.dp,
                    bottom = 4.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RegisterBackButton(
                onBack = {
                    if(state.currentStep == RegisterStep.CONTA){
                        onIntent(RegisterIntent.Leave)
                    }else{
                        onIntent(RegisterIntent.Back)
                    }
                }
            )
        }

        // ── Screen title ──────────────────────────────────────────────────────
        Text(
            text     = "CRIAR CONTA",
            style    = type.titleLarge,
            color    = colors.onBackground,
            modifier = Modifier.padding(horizontal = RegisterDimens.screenHorizontal),
        )

        Spacer(Modifier.height(16.dp))

        // ── Step bar ──────────────────────────────────────────────────────────
        RegisterStepBar(
            currentStep = state.currentStep,
            modifier    = Modifier.padding(horizontal = RegisterDimens.screenHorizontal),
        )

        Spacer(Modifier.height(24.dp))

        // ── CLASSE extra heading ──────────────────────────────────────────────
        if (state.currentStep == RegisterStep.CLASSE) {
            Text(
                text     = "ESCOLHA SUA CLASSE",
                style    = type.titleLarge,
                modifier = Modifier.padding(horizontal = RegisterDimens.screenHorizontal),
            )
            Spacer(Modifier.height(8.dp))
        }

        // ── Animated step content ─────────────────────────────────────────────
        AnimatedContent(
            targetState    = state.currentStep,
            transitionSpec = {
                val fwd = targetState.index > initialState.index
                (slideInHorizontally(tween(320)) { if (fwd) it else -it } +
                        fadeIn(tween(320))) togetherWith
                        (slideOutHorizontally(tween(320)) { if (fwd) -it else it } +
                                fadeOut(tween(200)))
            },
            modifier = Modifier.fillMaxWidth().weight(1f),
            label = "registerStep",
        ) { step ->
            when (step) {
                RegisterStep.CONTA     -> ContaStep(state = state, onIntent = onIntent)
                RegisterStep.PERFIL    -> PerfilStep(state = state, onIntent = onIntent)
                RegisterStep.CLASSE    -> ClasseStep(state = state, onIntent = onIntent)
                RegisterStep.OBJETIVOS -> ObjetivosStep(state = state, onIntent = onIntent)
            }
        }
    }
}