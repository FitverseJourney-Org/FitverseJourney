package org.fitverse.project.destinations.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.domain.model.authentication.login.LoginAction
import com.example.domain.model.dbLocal.language.Language
import com.example.presentation.components.snackbar.AppSnackbarHost
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.navigationState.LoginNavigation
import com.example.presentation.screens.ui.authentication.login.LoginScreen
import com.example.presentation.screens.ui.authentication.login.LoginViewModel
import com.example.presentation.screens.ui.authentication.login.components.AnimatedLoginBackground
import com.example.presentation.screens.ui.setupLanguage.SetupLanguageScreen
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginDestination(
    toRegister: () -> Unit,
    toForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit,
    toHome: () -> Unit
) {
    val viewmodel = koinInject<LoginViewModel>()
    val state by viewmodel.state.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val keyboardController = LocalSoftwareKeyboardController.current

    val showLanguageScreen = remember { mutableStateOf(false) }

    LaunchedEffect(state.snackBarData) {
        state.snackBarData?.let { event ->
            snackBarHostState.showSnackbar(
                message = event.message,
                withDismissAction = true
            )
            viewmodel.consumeSnackBar()
        }
    }

    LaunchedEffect(state.processLogin){
        if(state.processLogin){
            toHome()
        }
    }

    LaunchedEffect(true){
        viewmodel.navigationState.collectLatest {
            when(it){
                is LoginNavigation.ToRegister -> {
                    toRegister()
                }
                is LoginNavigation.ToResetPassword -> {
                    toForgotPassword()
                }
                is LoginNavigation.ToHome -> {
                    toHome()
                }
            }
        }
    }



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedLoginBackground()
        LoginScreen(
            state = state,
            viewmodel = viewmodel,
            currentLanguage = state.language,
            snackBarHost = {
                AppSnackbarHost(
                    snackbarHostState = snackBarHostState,
                    snackbarType = state.snackBarData?.type ?: SnackbarType.INFO
                )
            },
            onEmailChanged = {
                viewmodel.onAction(LoginAction.EmailChanged(it))
            },
            onPasswordChanged = {
                viewmodel.onAction(LoginAction.PasswordChanged(it))
            },
            onTogglePasswordVisibility = {
                viewmodel.onAction(LoginAction.TogglePasswordVisibility)
            },
            onLoginClick = {
                keyboardController?.hide()
                viewmodel.onAction(LoginAction.LoginClicked)
            },
            navigateToRegister = {
                viewmodel.onAction(LoginAction.NavigateToRegister)
            },
            navigateToForgotPassword = {
                viewmodel.onAction(LoginAction.NavigateToForgotPassword)
            },
            navigateToHome = {
                viewmodel.onAction(LoginAction.NavigateToHome)
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
                        viewmodel.onAction(LoginAction.LanguageChanged(selectedLanguage))
                        showLanguageScreen.value = false
//                        onConfirmLanguage(selectedLanguage)
                    },
                    currentLanguage = state.language,
                    exit = {
                        showLanguageScreen.value = false
                    }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize()) {

                }
            }
        }
    }

}