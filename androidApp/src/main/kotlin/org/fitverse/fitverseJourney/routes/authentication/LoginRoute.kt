package org.fitverse.fitverseJourney.routes.authentication

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.login.LoginAction
import com.example.domain.model.dbLocal.language.Language
import com.example.domain.model.onboarding.OnboardingAnimationTopics
import com.example.expect.FitnessLottieAnimation
import com.example.presentation.components.snackbar.AppSnackbarHost
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.presenter.AppPresenter
import com.example.presentation.presenter.authentication.LoginPresenter
import com.example.presentation.screens.authentication.login.LoginScreen
import com.example.presentation.screens.authentication.login.components.AnimatedLoginBackground
import com.example.presentation.screens.setupLanguage.SetupLanguageScreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginRoute(
    presenter: LoginPresenter,
    onLoginSuccess: () -> Unit,
    onConfirmLanguage: (Language) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToResetPassword: () -> Unit,
) {
    val state by presenter.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val showLanguageScreen = remember { mutableStateOf(false) }

    LaunchedEffect(state.snackBarData) {
        state.snackBarData?.let { event ->
            snackBarHostState.showSnackbar(
                message = event.message,
                withDismissAction = true
            )
            presenter.consumeSnackBar()
        }
    }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            onLoginSuccess()
        }
    }





    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedLoginBackground()

        LoginScreen(
            state = state,
            currentLanguage = state.language,
            snackBarHost = {
                AppSnackbarHost(
                    snackbarHostState = snackBarHostState,
                    snackbarType = state.snackBarData?.type ?: SnackbarType.INFO
                )
            },
            onEmailChanged = {
                presenter.onAction(LoginAction.EmailChanged(it))
            },
            onPasswordChanged = {
                presenter.onAction(LoginAction.PasswordChanged(it))
            },
            onTogglePasswordVisibility = {
                presenter.onAction(LoginAction.TogglePasswordVisibility)
            },
            onLoginClick = {
                keyboardController?.hide()
                presenter.onAction(LoginAction.Login)
            },
            onNavigateToRegister = {
                onNavigateToRegister()
            },
            onNavigateToForgotPassword = {
                onNavigateToResetPassword()
            },
            showLanguageScreen = {
                showLanguageScreen.value = !showLanguageScreen.value
            }
        )

        AnimatedContent(
            targetState = showLanguageScreen.value,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                if (targetState) {
                    (slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    ) + fadeIn(animationSpec = tween(100)))
                        .with(
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(100))
                        )
                } else {
                    (slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(500)))
                        .with(
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = tween(1200)
                            )
                        )
                }.using(SizeTransform(clip = false))
            }
        ) { shown ->
            if (shown) {
                SetupLanguageScreen(
                    // agora recebe o Language selecionado
                    onConfirmLanguage = { selectedLanguage: Language ->
                        presenter.onAction(LoginAction.LanguageChanged(selectedLanguage))
                        showLanguageScreen.value = false
                        onConfirmLanguage(selectedLanguage)
                    },
                    currentLanguage = state.language,
                    exit = {
                        showLanguageScreen.value = false
                    }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize()) { /* vazio, só ocupa espaço */ }
            }
        }
    }
}

