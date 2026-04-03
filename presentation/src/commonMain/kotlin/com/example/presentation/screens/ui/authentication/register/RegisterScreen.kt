package com.example.presentation.screens.ui.authentication.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.authentication.register.RegisterPage
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.AvatarStatusDialog
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageCredentials
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageGoals
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageIntroduction
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageExperienceLevel
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageMacros
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageMetrics
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageSuccess
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageAvatar
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageStepGender
import com.example.presentation.screens.ui.authentication.register.state.RegisterState
import com.example.presentation.screens.widgets.FitVerseButton
import com.example.presentation.screens.widgets.FitverseIconBack
import com.example.presentation.screens.widgets.FitverseIconClose

private val SecondaryBlue = Color(0xFF2563EB)
private val TertiaryGreen = Color(0xFF10B981)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    snackBarHost: @Composable () -> Unit,
) {
    val cs = MaterialTheme.colorScheme
    val totalPages = RegisterPage.entries.size.toFloat()
    val targetProgress = (state.page.ordinal + 1) / totalPages
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 500),
        label = "RegisterProgress"
    )
    Scaffold(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        containerColor = Color.Transparent, // Fundo Neutro #0A0B0F
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color.White.copy(alpha = 0.05f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedProgress)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(SecondaryBlue, TertiaryGreen)
                                )
                            )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        FitverseIconBack(
                            onBack = {
                                onAction(RegisterAction.Back)
                            }
                        )
                        Text(
                            text = "PASSO ${state.page.ordinal + 1} DE ${RegisterPage.entries.size}",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }
                    FitverseIconClose(
                        onClose = {
                            onAction(RegisterAction.Exit)
                        }
                    )
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier.navigationBarsPadding().padding(24.dp),
                contentAlignment = Alignment.Center
            ){
                FitVerseButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if(state.page == RegisterPage.Success) "VOLTAR PARA LOGIN" else if(state.page == RegisterPage.Credentials) "FINALIZAR" else "AVANÇAR",
                    topColor = cs.primary,
                    edgeColor = cs.outline, // Ou uma cor mais escura que o primary
                    textColor = cs.onPrimary,
                    isLoading = state.isLoading,
                    onClick = {
                        if(state.page == RegisterPage.Success) {
                            onAction(RegisterAction.Exit)
                        }else{
                            onAction(RegisterAction.Next)
                        }
                    },
                )

            }
        }
    ) { padding ->
        AnimatedContent(
            modifier = Modifier.padding(padding),
            targetState = state.page,
            label = "RegisterPages",
            transitionSpec = {
                val isForward = targetState.ordinal > initialState.ordinal
                if (isForward) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(400)
                    ) togetherWith
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400)
                            )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(400)
                    ) togetherWith
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400)
                            )
                }
            }
        ) { page ->
            // Mapeamento das páginas no fluxo
            when (page) {
                RegisterPage.Profile -> RegisterPageIntroduction(state, onAction)
                RegisterPage.Metrics -> RegisterPageMetrics(state, onAction)
                RegisterPage.Goals -> RegisterPageGoals(state, onAction)
                RegisterPage.Gender -> RegisterPageStepGender(state, onAction)
                RegisterPage.Level -> RegisterPageExperienceLevel(state, onAction)
                RegisterPage.Avatar -> RegisterPageAvatar(state, onAction)
                RegisterPage.Macros -> RegisterPageMacros(state, onAction) // NOVA TELA AQUI
                RegisterPage.Credentials -> RegisterPageCredentials(state, onAction)
                RegisterPage.Success -> RegisterPageSuccess(state, onAction)
            }
        }

        if (state.dialogStatusAvatar) { // <-- Use o nome real da sua variável boolean aqui
            AvatarStatusDialog(
                onDismiss = {
                    onAction(RegisterAction.DialogStatusAvatar(value = false))
                }
            )
        }
    }
}