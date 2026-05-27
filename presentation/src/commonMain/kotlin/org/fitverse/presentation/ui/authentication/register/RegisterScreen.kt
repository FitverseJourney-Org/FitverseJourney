package org.fitverse.presentation.ui.authentication.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.ui.authentication.register.components.RegisterBackButton
import org.fitverse.presentation.ui.authentication.register.components.RegisterStepBar
import org.fitverse.presentation.ui.authentication.register.pages.ClasseStep
import org.fitverse.presentation.ui.authentication.register.pages.ContaStep
import org.fitverse.presentation.ui.authentication.register.pages.GoalsStep
import org.fitverse.presentation.ui.authentication.register.pages.PerfilStep
import org.fitverse.presentation.ui.authentication.register.states.RegisterIntent
import org.fitverse.presentation.ui.authentication.register.states.RegisterStep
import org.fitverse.presentation.ui.authentication.register.states.RegisterUiState
import org.fitverse.presentation.theme.RegisterDimens
import org.fitverse.presentation.utils.getLocaleDatePattern
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.register_choose_class_title
import fitversejourneyapp.presentation.generated.resources.register_screen_title
import fitversejourneyapp.presentation.generated.resources.register_step_classe
import fitversejourneyapp.presentation.generated.resources.register_step_conta
import fitversejourneyapp.presentation.generated.resources.register_step_objetivos
import fitversejourneyapp.presentation.generated.resources.register_step_perfil
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onAction: (RegisterIntent) -> Unit,
    onBack: () -> Unit = {},
) {
    val pattern      = getLocaleDatePattern()
    val snackbarHost = remember { SnackbarHostState() }

    LaunchedEffect(pattern) { onAction(RegisterIntent.LocaleResolved(pattern)) }

    LaunchedEffect(
        key1 = state.registrationComplete,
        key2 = state.registrationCancellable
    ) {
        if (state.registrationComplete) {
            delay(3000)
            onBack()
        }
        if (state.registrationCancellable) onBack()
    }

    LaunchedEffect(state.snackbarEvent) {
        state.snackbarEvent?.let { event ->
            snackbarHost.showSnackbar(
                message  = event.message,
                duration = SnackbarDuration.Short,
            )
            onAction(RegisterIntent.SnackbarConsumed)
        }
    }

    RegisterScreenContent(
        state = state,
        snackbarHost = snackbarHost,
        onIntent = { intent ->
            if (intent == RegisterIntent.Leave) onBack()
            else onAction(intent)
        }
    )
}

@Composable
private fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    state: RegisterUiState,
    snackbarHost: SnackbarHostState,
    onIntent: (RegisterIntent) -> Unit,

    ) {
    val colors = MaterialTheme.colorScheme
    val type   = MaterialTheme.typography

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        start  = RegisterDimens.screenHorizontal - 12.dp,
                        end    = RegisterDimens.screenHorizontal,
                        top    = 8.dp,
                        bottom = 4.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RegisterBackButton(
                    onBack = {
                        if (state.currentStep == RegisterStep.CONTA) {
                            onIntent(RegisterIntent.Leave)
                        } else {
                            onIntent(RegisterIntent.Back)
                        }
                    }
                )
            }
        },
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
        containerColor = colors.background,
    ){
        Column(
            modifier = modifier.fillMaxSize().padding(it),
        ) {
            // ── Screen title ─────────────────────────────────────────────────────
            Text(
                text     = stringResource(Res.string.register_screen_title),
                style    = type.displaySmall,
                color    = colors.onBackground,

                modifier = Modifier.padding(horizontal = RegisterDimens.screenHorizontal),
            )

            Spacer(Modifier.height(16.dp))

            // ── Step bar ─────────────────────────────────────────────────────────
            RegisterStepBar(
                currentStep = state.currentStep,
                modifier = Modifier.padding(horizontal = RegisterDimens.screenHorizontal),
            )

            Spacer(Modifier.height(24.dp))

            // ── CLASSE extra heading ─────────────────────────────────────────────
            if (state.currentStep == RegisterStep.CLASSE) {
                Text(
                    text     = stringResource(Res.string.register_choose_class_title),
                    style    = type.titleLarge,
                    color    = colors.onBackground,
                    modifier = Modifier.padding(horizontal = RegisterDimens.screenHorizontal),
                )
                Spacer(Modifier.height(8.dp))
            }

            // ── Animated step content ────────────────────────────────────────────
            AnimatedContent(
                targetState    = state.currentStep,
                transitionSpec = {
                    val fwd = targetState.index > initialState.index
                    (slideInHorizontally(tween(320)) { if (fwd) it else -it } +
                            fadeIn(tween(320))) togetherWith
                            (slideOutHorizontally(tween(320)) { if (fwd) -it else it } +
                                    fadeOut(tween(200)))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                label = "registerStep",
            ) { step ->
                when (step) {
                    RegisterStep.CONTA     -> ContaStep(state = state, onIntent = onIntent)
                    RegisterStep.PERFIL    -> PerfilStep(state = state, onIntent = onIntent)
                    RegisterStep.CLASSE    -> ClasseStep(state = state, onIntent = onIntent)
                    RegisterStep.OBJETIVOS -> GoalsStep(state = state, onIntent = onIntent)
                }
            }
        }

    }
}

@Composable
fun RegisterStep.label(): String = when (this) {
    RegisterStep.CONTA     -> stringResource(Res.string.register_step_conta)
    RegisterStep.PERFIL    -> stringResource(Res.string.register_step_perfil)
    RegisterStep.CLASSE    -> stringResource(Res.string.register_step_classe)
    RegisterStep.OBJETIVOS -> stringResource(Res.string.register_step_objetivos)
}


