package com.example.presentation.screens.ui.authentication.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.RegisterAction
import com.example.domain.model.authentication.register.RegisterPage
import com.example.presentation.screens.ui.authentication.login.components.AuthDefaultButton
import com.example.presentation.screens.ui.authentication.register.components.RegisterProgressBar
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageCredentials
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageGoals
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageLevel
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageProfile
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageSuccess
import com.example.presentation.screens.ui.authentication.register.components.RegisterProgressBar
import com.example.presentation.screens.ui.authentication.register.state.RegisterState
import com.example.presentation.theme.backgroundBrush
import com.example.presentation.theme.transparent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterState,
    animatedProgress: Float,
    snackBarHostState: @Composable () -> Unit,
    onAction: (RegisterAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                CenterAlignedTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = transparent
                    ),
                    title = {
                        Text(
                            text = "${(animatedProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                )

                RegisterProgressBar(
                    progress = animatedProgress
                )
            }
        },
        snackbarHost = snackBarHostState,
        bottomBar = {
            RegisterBottomBar(
                modifier = Modifier.windowInsetsPadding(
                    insets = WindowInsets.navigationBars
                ).padding(horizontal = 16.dp, vertical = 5.dp),
                isFirstPage = state.page == RegisterPage.Profile,
                isLastPage = state.page == RegisterPage.Success,
                isLoading = state.isLoading,
                onBack = {
                    if (state.page == RegisterPage.Profile) {
                        onAction(RegisterAction.Exit)
                    } else {
                        onAction(RegisterAction.Back)
                    }
                },
                txtProcess = {
                    if(state.page == RegisterPage.Credentials){
                        "Create"
                    }else{
                        "Next"
                    }
                },
                txtBack = {
                    if(state.page == RegisterPage.Profile){
                        "Exit"
                    }else{
                        "Back"
                    }
                },
                onNext = {
                    when (state.page) {
                        RegisterPage.Success -> {
                            onAction(RegisterAction.Exit)
                        }
                        RegisterPage.Credentials -> {
                            onAction(RegisterAction.Submit)
                        }
                        else -> {
                            onAction(RegisterAction.Next)
                        }
                    }
                }
            )
        },
    ) { padding ->

        AnimatedContent(
            modifier = Modifier.padding(padding).padding(16.dp),
            targetState = state.page
        ) { page ->
            when (page) {
                RegisterPage.Profile -> RegisterPageProfile(state, onAction)
                RegisterPage.Goals -> RegisterPageGoals(state, onAction)
                RegisterPage.Level -> RegisterPageLevel(state, onAction)
                RegisterPage.Credentials -> RegisterPageCredentials(state, onAction)
                RegisterPage.Success -> RegisterPageSuccess(state, onAction)
            }
        }
    }
}

@Composable
fun RegisterBottomBar(
    modifier: Modifier,
    isFirstPage: Boolean,
    isLastPage: Boolean,
    isLoading: Boolean,
    txtProcess: () -> String,
    txtBack: () -> String,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row {
            if(!isLastPage){
                AuthDefaultButton(
                    modifier = Modifier.weight(1f),
                    text = txtBack(),
                    onClick = onBack,
                    enabled = true
                )

                Spacer(Modifier.width(12.dp))

                AuthDefaultButton(
                    modifier = Modifier.weight(1f),
                    text = txtProcess(),
                    onClick = {
                        onNext()
                    },
                    isLoading = isLoading,
                    enabled = true
                )
            }
        }
    }
}
