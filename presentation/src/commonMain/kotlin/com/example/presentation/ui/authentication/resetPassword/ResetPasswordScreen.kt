package org.fitverse.presentation.ui.authentication.resetPassword

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.ui.authentication.login.components.FitverseOutlinedTextField
import org.fitverse.presentation.ui.authentication.register.components.RegisterBackButton
import org.fitverse.presentation.ui.authentication.resetPassword.states.ResetPasswordAction
import org.fitverse.presentation.ui.authentication.resetPassword.states.ResetPasswordState
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.theme.RegisterDimens
import org.fitverse.presentation.widgets.FitVerseSpacer
import org.fitverse.presentation.widgets.FitverseButton
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.reset_button_send
import fitversejourneyapp.presentation.generated.resources.reset_email_label
import fitversejourneyapp.presentation.generated.resources.reset_email_placeholder
import fitversejourneyapp.presentation.generated.resources.reset_subtitle
import fitversejourneyapp.presentation.generated.resources.reset_success_content_description
import fitversejourneyapp.presentation.generated.resources.reset_success_subtitle
import fitversejourneyapp.presentation.generated.resources.reset_success_title
import fitversejourneyapp.presentation.generated.resources.reset_title
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResetPasswordScreen(
    state: ResetPasswordState,
    viewmodel: ResetPasswordViewModel,
    onNavigateBack: () -> Unit,
) {
    val snackbarHost = remember { SnackbarHostState() }

    LaunchedEffect(state.isEmailSent) {
        if (state.isEmailSent) {
            delay(3_000L)
            onNavigateBack()
        }
    }

    LaunchedEffect(state.snackBarData?.id) {
        state.snackBarData?.let { data ->
            snackbarHost.showSnackbar(
                message  = data.message,
                duration = SnackbarDuration.Short,
            )
            viewmodel.onSnackbarConsumed()
        }
    }

    ResetPasswordScreenContent(
        state = state,
        snackbarHost = snackbarHost,
        onIntent = { action ->
            if (action == ResetPasswordAction.BtnBack) onNavigateBack() else viewmodel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreenContent(
    state    : ResetPasswordState,
    snackbarHost: SnackbarHostState,
    onIntent : (ResetPasswordAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

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
                    onBack = { onIntent(ResetPasswordAction.BtnBack) }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHost) { data ->
                Snackbar(
                    snackbarData   = data,
                    containerColor = if (state.snackBarData?.type == org.fitverse.domain.models.snackbar.SnackbarType.ERROR)
                        MaterialTheme.colorScheme.errorContainer
                    else
                        MaterialTheme.colorScheme.primaryContainer,
                    contentColor   = if (state.snackBarData?.type == org.fitverse.domain.models.snackbar.SnackbarType.ERROR)
                        MaterialTheme.colorScheme.onErrorContainer
                    else
                        MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        },
        containerColor = FitColors.Bg,
        content = { paddingValues ->
            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                targetState = state.isEmailSent,
                transitionSpec = {
                    fadeIn(animationSpec = tween(600)) togetherWith
                            fadeOut(animationSpec = tween(300))
                },
                label = "ResetPasswordContent",
            ) { sent ->
                if (sent) {
                    // ── Success State ─────────────────────────────────────────
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    color = Color(0xFF2ECC40),
                                    shape = RoundedCornerShape(8.dp),
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector        = Icons.Default.Check,
                                contentDescription = stringResource(Res.string.reset_success_content_description),
                                tint               = Color.White,
                                modifier           = Modifier.size(44.dp),
                            )
                        }

                        FitVerseSpacer(value = 24.dp, vertical = true)

                        Text(
                            text       = stringResource(Res.string.reset_success_title),
                            style      = MaterialTheme.typography.labelMedium,
                            color      = Color(0xFF2ECC40),
                            fontWeight = FontWeight.Bold,
                            fontSize   = 22.sp,
                        )

                        FitVerseSpacer(value = 8.dp, vertical = true)

                        Text(
                            text     = stringResource(Res.string.reset_success_subtitle),
                            fontSize = 16.sp,
                            color    = FitColors.TextMuted,
                        )
                    }
                } else {
                    // ── Form State ────────────────────────────────────────────
                    Column(modifier = Modifier.fillMaxSize()) {
                        FitVerseSpacer(value = 8.dp, vertical = true)

                        Text(
                            modifier = Modifier.padding(5.dp),
                            text     = "🔑",
                            fontSize = 50.sp,
                        )

                        FitVerseSpacer(value = 8.dp, vertical = true)

                        Text(
                            text       = stringResource(Res.string.reset_title),
                            style      = MaterialTheme.typography.labelMedium,
                            color      = FitColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 28.sp,
                        )

                        FitVerseSpacer(value = 8.dp, vertical = true)

                        Text(
                            text       = stringResource(Res.string.reset_subtitle),
                            fontSize   = 16.sp,
                            color      = FitColors.TextMuted,
                            lineHeight = 19.sp,
                        )

                        FitVerseSpacer(value = 28.dp, vertical = true)

                        FitverseOutlinedTextField(
                            value = state.email,
                            subtitle = stringResource(Res.string.reset_email_label),
                            onValueChange = { onIntent(ResetPasswordAction.EmailChanged(it)) },
                            placeholder = stringResource(Res.string.reset_email_placeholder),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                        )

                        FitVerseSpacer(value = 20.dp, vertical = true)

                        FitverseButton(
                            text = stringResource(Res.string.reset_button_send),
                            onClick = {
                                onIntent(ResetPasswordAction.BtnSubmit)
                                keyboardController?.hide()
                            },
                            enabled = state.email.isNotEmpty(),
                            isLoading = state.isLoading
                        )
                    }
                }
            }
        }
    )
}