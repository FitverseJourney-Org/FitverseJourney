package org.fitverse.project.destinations.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
 import com.example.presentation.navigationState.LoginNavigation
import com.example.presentation.ui.LanguageViewModel
import com.example.presentation.ui.authentication.login.LoginScreen
import com.example.presentation.ui.authentication.login.LoginViewModel
import com.example.presentation.ui.authentication.login.states.LoginAction
import com.example.presentation.ui.setupLanguage.SetupLanguageScreen
import com.example.presentation.utils.LanguageAvailableApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

// ─── Estados internos da tela ────────────────────────────────────────────────

private sealed interface LoginUiLayer {
    data object Form : LoginUiLayer          // tela normal de login
    data object Language : LoginUiLayer      // seletor de idioma (slide-over)
    data object ApplyingLanguage : LoginUiLayer // loading + check após confirmação
}

// ─── Destination ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginDestination(
    toRegister: () -> Unit,
    toForgotPassword: () -> Unit,
    toHome: () -> Unit,
    toLoadingLanguage: () -> Unit,
) {
    val viewmodel        = koinInject<LoginViewModel>()
    val viewmodelLanguage = koinInject<LanguageViewModel>()

    val state               by viewmodel.state.collectAsState()
    val currentLanguageCode by viewmodelLanguage.languageCode.collectAsState()

    val currentAppLanguageItem = remember(currentLanguageCode) {
        LanguageAvailableApp().fromCode(currentLanguageCode)
    }

    val snackBarHostState   = remember { SnackbarHostState() }
    val keyboardController  = LocalSoftwareKeyboardController.current

    // Estado central que controla QUAL camada está visível
    var uiLayer by remember { mutableStateOf<LoginUiLayer>(LoginUiLayer.Form) }

    // ── Side-effects ──────────────────────────────────────────────────────────

    // Snackbar de erros
    LaunchedEffect(state.snackBarData) {
        state.snackBarData?.let { event ->
            snackBarHostState.showSnackbar(message = event.message, withDismissAction = true)
            viewmodel.consumeSnackBar()
        }
    }
    // Navegação via ViewModel
    LaunchedEffect(Unit) {
        viewmodel.navigationState.collectLatest {
            when (it) {
                is LoginNavigation.ToRegister      -> toRegister()
                is LoginNavigation.ToResetPassword -> toForgotPassword()
                is LoginNavigation.ToHome          -> toHome()
            }
        }
    }

    // Após aplicar idioma: feedback visual (1.5s) → navega para recarga
    LaunchedEffect(uiLayer) {
        if (uiLayer == LoginUiLayer.ApplyingLanguage) {
            delay(1_500L)
            toLoadingLanguage()
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        // Camada 1 — formulário de login (sempre renderizado como base)
        LoginScreen(
            state = state,
            snackBarHostState = snackBarHostState,
            onAction          = viewmodel::onAction,  // ou { viewModel.onAction(it) }
            onLogin = { _, _ ->
                viewmodel.onLoginClick()
                keyboardController?.hide()
            },
            onForgotPassword = { viewmodel.onAction(LoginAction.NavigateToForgotPassword) },
            onCreateAccount  = { viewmodel.onAction(LoginAction.NavigateToRegister) },
            onSocialLogin    = { keyboardController?.hide() },
            // ← botão de idioma na TopBar chama isso:
            onOpenLanguage   = { uiLayer = LoginUiLayer.Language },
        )

        // Camada 2 — seletor de idioma (slide da esquerda para a direita)
        AnimatedContent(
            targetState = uiLayer,
            transitionSpec = {
                when {
                    // Form → Language: entra da esquerda, login sai para a direita
                    targetState == LoginUiLayer.Language -> {
                        (slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) +
                                fadeIn(tween(200))) togetherWith
                                (slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) +
                                        fadeOut(tween(100)))
                    }
                    // Language → Form: entra da direita, idioma sai para a esquerda
                    initialState == LoginUiLayer.Language -> {
                        (slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) +
                                fadeIn(tween(200))) togetherWith
                                (slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) +
                                        fadeOut(tween(100)))
                    }
                    // qualquer outro: fade simples
                    else -> fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                }.using(SizeTransform(clip = false))
            },
            label = "LoginUiLayerTransition",
        ) { layer ->
            when (layer) {

                // ── Formulário de login (slot vazio — já renderizado atrás) ──
                LoginUiLayer.Form -> Box(Modifier.fillMaxSize())

                // ── Seletor de idioma ─────────────────────────────────────────
                LoginUiLayer.Language -> {
                    SetupLanguageScreen(
                        currentAppLanguageItem = currentAppLanguageItem,
                        exit = { uiLayer = LoginUiLayer.Form },
                        onConfirmLanguage = { selectedItem ->
                            viewmodelLanguage.switchLanguage(selectedItem.code.iso)
                            uiLayer = LoginUiLayer.ApplyingLanguage
                        },
                    )
                }

                // ── Feedback de aplicação de idioma ───────────────────────────
                LoginUiLayer.ApplyingLanguage -> {
                    ApplyingLanguageOverlay()
                }
            }
        }
    }
}

// ─── Overlay de feedback ao trocar idioma ─────────────────────────────────────

@Composable
private fun ApplyingLanguageOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = true,           // só entra uma vez
            transitionSpec = {
                (scaleIn(tween(400)) + fadeIn(tween(300))) togetherWith
                        (scaleOut(tween(200)) + fadeOut(tween(200)))
            },
            label = "ApplyingLanguageContent",
        ) {
            ApplyingLanguageCard()
        }
    }
}

@Composable
private fun ApplyingLanguageCard() {
    var showCheck by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(700L)      // tempo de "carregar"
        showCheck = true
    }

    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = showCheck,
            transitionSpec = {
                (scaleIn(tween(300)) + fadeIn(tween(200))) togetherWith
                        (scaleOut(tween(200)) + fadeOut(tween(150)))
            },
            label = "CheckOrSpinner",
        ) { check ->
            if (check) {
                // ✓ verde — idioma aplicado
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF2ECC40)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector        = Icons.Default.Check,
                        contentDescription = null,
                        tint               = Color.White,
                        modifier           = Modifier.size(44.dp),
                    )
                }
            } else {
                // Spinner enquanto aplica
                CircularProgressIndicator(
                    modifier  = Modifier.size(48.dp),
                    color     = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp,
                )
            }
        }

        // Label abaixo do ícone
        AnimatedVisibility(
            visible = showCheck,
            enter   = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(300)),
            exit    = slideOutVertically(targetOffsetY  = { it }) + fadeOut(tween(200)),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Text(
                text     = "Aplicando...",
                fontSize = 13.sp,
                color    = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .run { this }, // apenas para evitar warning de escopo
            )
        }
    }
}