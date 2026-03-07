package org.fitverse.fitverseJourney.routes.authentication

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
import com.example.presentation.screens.ui.authentication.login.LoginViewModel
import com.example.presentation.screens.ui.authentication.login.LoginScreen
import com.example.presentation.screens.ui.authentication.login.components.AnimatedLoginBackground
import com.example.presentation.screens.ui.setupLanguage.SetupLanguageScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginRoute(
    presenter: LoginViewModel,
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






}

